package br.org.rfdouro.mvcsec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.org.rfdouro.mvcsec.security.data.User;
import br.org.rfdouro.mvcsec.security.data.UserRepository;
import jakarta.transaction.Transactional;

// cria um objeto de configuração
@Configuration
// habilita a segurança web com a possibilidade de indicar a cadeia de filtros
// de segurança
@EnableWebSecurity
public class SecurityConfig implements CommandLineRunner {

  // injeta um objeto de controle de transações de banco
  @Autowired
  private PlatformTransactionManager transactionManager;

  // injeta um objeto que manipula usuários (User::UserDetails)
  @Autowired
  private UserRepository userRepository;

  // método gerenciado que retorna um objeto usado no contexto de segurança para
  // buscar um usuário pelo seu login e usá-lo na validação de permissões
  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // considera o UserName de UserDetails como sendo o login do User (classe aqui
        // do projeto)
        return userRepository.findByLogin(username).get();
      }
    };
  }

  // https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html
  /*
   * Quando você inclui a dependência spring-boot-starter-security
   * ou usa a anotação @EnableWebSecurity, o Spring Security
   * adicionará seu suporte de logout e, por padrão, responderá
   * a GET /logout e POST /logout.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(r -> {
      // permissão irrestrita nesses padrões de url
      r.requestMatchers("/ajuda", "/login", "/login**", "/logout", "/resources/**", "/static/**", "/styles/**")
          .permitAll()
          // para as urls aqui precisa ter a autorização ADMIN
          .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
          // todos os outros requests precisam estar autenticados
          .requestMatchers("/**").authenticated();
    });
    http.formLogin(f -> {
      f.permitAll(); // permite acesso irrestrito
      f.loginPage("/login"); // indica que o endpoint será esse usando o controller LoginController
      f.usernameParameter("login"); // altera o username para login como campo de login
      f.loginProcessingUrl("/processalogin"); // a URL de processar login é essa
      f.defaultSuccessUrl("/index"); // em caso de sucesso encaminha para o index
      f.failureUrl("/login?error=true"); // em caso de erro retorna para o login
    });
    http.csrf(c -> {
      c.disable(); // desabilita a verificação para ataue CSRF
    });
    return http.build();
  }

  // adiciona usuários padrão da aplicação
  @Transactional
  private void addUsers() {
    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
    definition.setTimeout(-1);

    TransactionStatus status = transactionManager.getTransaction(definition);

    System.out.println(userRepository.count());

    try {

      User u = new User();
      u.setLogin("admin");
      u.setPassword("{noop}1234");
      u.setRoles("ROLE_USER,ROLE_ADMIN");
      userRepository.save(u);

      u = new User();
      u.setLogin("usuario");
      u.setPassword("{noop}1234");
      u.setRoles("ROLE_USER");
      userRepository.save(u);

      transactionManager.commit(status);

    } catch (Exception ex) {
      transactionManager.rollback(status);
    }
  }

  @Override
  public void run(String... args) throws Exception {
    // executa a adição de usuários da aplicação no momento da execução
    addUsers();
  }

}

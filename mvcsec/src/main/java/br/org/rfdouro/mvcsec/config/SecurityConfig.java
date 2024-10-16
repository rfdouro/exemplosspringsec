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

@Configuration
@EnableWebSecurity
public class SecurityConfig implements CommandLineRunner {

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private UserRepository userRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username).get();
      }
    };
  }

  //https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html
  /*
   * Quando você inclui a dependência spring-boot-starter-security 
   * ou usa a anotação @EnableWebSecurity, o Spring Security 
   * adicionará seu suporte de logout e, por padrão, responderá 
   * a GET /logout e POST /logout.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(r -> {
      r.requestMatchers("/ajuda", "/login", "/login**", "/logout", "/resources/**", "/static/**", "/styles/**").permitAll()
          .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
          .requestMatchers("/**").authenticated();
    });
    http.formLogin(f -> {
      f.permitAll();
      f.loginPage("/login");
      f.usernameParameter("login");
      f.loginProcessingUrl("/processalogin");
      f.defaultSuccessUrl("/index");
      f.failureUrl("/login?error=true");
    });
    http.csrf(c -> {
      c.disable();
    });
    return http.build();
  }

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
    addUsers();
  }

}

package br.org.rfdouro.apisec.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.OncePerRequestFilter;

import br.org.rfdouro.apisec.security.User;
import br.org.rfdouro.apisec.security.UserRepository;
import br.org.rfdouro.apisec.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@EnableWebSecurity
@Configuration
public class APIConfig implements CommandLineRunner {
  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  /**
   * não é utilizado mas em geral serve para encriptar as senhas dos usuários
   * @return
   */
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((auth) -> {
      // auth.anyRequest().permitAll();
      auth.requestMatchers("/alunos/**", "/tarefas/**").hasRole("USER")
          .requestMatchers("/auth**", "/index.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
          .anyRequest().authenticated();
    });
    http.cors(t -> {
      t.disable();
    });
    http.csrf(t -> {
      t.disable();
    });
    http.sessionManagement(t -> {
      t.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    });
    http.addFilterBefore(new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
        try {
          String authHeader = request.getHeader("Authorization");
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userName = jwtService.extractUsername(token);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
              UserDetails user = userRepository.findByLogin(userName).get();
              if (jwtService.isTokenValid(token, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);//aqui é q manda
              }
            }
          }
        } catch (Exception ex) {
        }
        filterChain.doFilter(request, response);
      }
    }, UsernamePasswordAuthenticationFilter.class);
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
      u.setPassword("1234");
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

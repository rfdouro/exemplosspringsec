# Projetos de Exemplo com *Security* usando `Spring Boot`

Os projetos aqui usam Spring Security

|Projeto|Descrição|
|-----|-----|
|apisec|Projeto de API para cadastro de alunos e tarefas (de alunos). Usa springdoc-openapi para gerar documentação.|
|mvcsec|Projeto de aplicação MVC com thymeleaf para templates.|

## Dados dos projetos

Ambos os projetos usam o conceito de cadeia de filtro com a implementação de um SecurityFilterChain

````Java
public SecurityFilterChain filterChain(HttpSecurity http)
````
No caso do projeto [apisec](apisec) é usado um filtro (*OncePerRequestFilter*) implementado como **classe anônima** que captura o TOKEN Bearer (JWT) que deve vir junto do cabeçalho da requisição com as informações do acesso uma vez que é uma aplicação REST (*stateless*). Esse TOKEN é obtido no *enpoint* de login.

No caso do projeto [mvcsec](mvcsec) é usada uma customização para busca de usuários através de seu login. Usado no contexto de segurança para identificação do usuário em Sessão e suas respectivas autorizações.

````Java
@Bean
public UserDetailsService userDetailsService() {
  return new UserDetailsService() {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return userRepository.findByLogin(username).get();
    }
  };
}
````

Ambos os projetos usam uma implementação de entidade que implementa *UserDetails* para ser utilizada no contexto de segurança (*SecurityContextHolder*).

```Java
// ...
@Table(name = "usuarios")
@Entity
public class User implements UserDetails {
  // ...
```
Deve-se observar ainda que no projeto **mvcsec** as senhas dos usuários devem ser armazenadas com um prefixo:
````Java
u.setPassword("{noop}1234");
````
Isso é devido ao fato de não usar senhas encriptadas, dessa forma o desenvolvedor deve '*dizer explicitamente*' que a senha é **aquela sem segurança** [Referência](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html).

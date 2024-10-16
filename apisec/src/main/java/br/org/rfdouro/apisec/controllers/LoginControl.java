package br.org.rfdouro.apisec.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.rfdouro.apisec.security.User;
import br.org.rfdouro.apisec.security.UserRepository;
import br.org.rfdouro.apisec.services.JwtService;

import java.util.Optional;
import java.util.Map;
import static java.util.Map.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/auth")
public class LoginControl {

 @Autowired
 JwtService jwtService;

 @Autowired
 UserRepository userRepository;

 @PostMapping
 public ResponseEntity<Map<String, String>> logar(String login, String password) {
  Optional<User> uo = userRepository.findByLogin(login);
  if (uo.isPresent()) {
   User u = uo.get();
   if (u.getPassword().equals(password)) {
    return ResponseEntity.ok().body(Map.ofEntries(entry("token", jwtService.generateToken(u))));
   } else {
    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(Map.ofEntries(entry("mensagem", "Senha inválida")));
   }
  }
  return ResponseEntity.status(HttpStatusCode.valueOf(200))
    .body(Map.ofEntries(entry("mensagem", "Usuário não encontrado")));
 }

}

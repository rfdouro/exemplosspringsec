package br.org.rfdouro.apisec.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "usuarios")
@Entity
public class User implements UserDetails {
 @Id
 private String login;
 private String password;

 public String getLogin() {
  return login;
 }

 public String getUserName(){
  return this.login;
 }

 public void setLogin(String login) {
  this.login = login;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 //aqui retorna as permissões
 @Override
 public Collection<? extends GrantedAuthority> getAuthorities() {
  return List.of(new SimpleGrantedAuthority("ROLE_USER"));
 }

 @Override
 public String getUsername() {
  return this.login;
 }

}

package br.org.rfdouro.mvcsec.security.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
 private String roles;

 public String getRoles() {
  return roles;
 }

 public void setRoles(String roles) {
  this.roles = roles;
 }

 public String getLogin() {
  return login;
 }

 public String getUserName() {
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

 // aqui retorna as permissões
 // são salvas em banco separadas por vírgula
 @Override
 public Collection<? extends GrantedAuthority> getAuthorities() {
  List<SimpleGrantedAuthority> l = Stream.of(this.roles.split(",")).map(r -> {return new SimpleGrantedAuthority(r);}).toList();
  return l;
 }

 @Override
 public String getUsername() {
  return this.login;
 }

}

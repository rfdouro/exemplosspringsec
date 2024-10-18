package br.org.rfdouro.mvcsec.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

//controle para tratar as operações voltadas ao login
@Controller
@RequestMapping
public class LoginControl {

 // trata GET em '/login' abrindo a página de login
 @GetMapping("/login")
 public String login() {
  return "login";
 }
}

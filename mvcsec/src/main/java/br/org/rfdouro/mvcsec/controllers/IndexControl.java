package br.org.rfdouro.mvcsec.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

//controle que tem alguns os mapeamentos da aplicação
@Controller
@RequestMapping
public class IndexControl {
 // mapeia GET para a página inicial
 @GetMapping({ "/", "/index" })
 public String index() {
  return "index";
 }

 // mapeia GET para a página admin
 @GetMapping("/admin")
 public String metodoAdmin() {
  return "paginaadmin";
 }

 // mapeia GET para a página de ajuda
 @GetMapping("/ajuda")
 public String metodoAjuda() {
  return "paginaajuda";
 }

}

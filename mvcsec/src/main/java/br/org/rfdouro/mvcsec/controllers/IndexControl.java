package br.org.rfdouro.mvcsec.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping
public class IndexControl {
 @GetMapping({ "/", "/index" })
 public String index() {
  return "index";
 }

 @GetMapping("/admin")
 public String metodoAdmin() {
  return "paginaadmin";
 }

 @GetMapping("/ajuda")
 public String metodoAjuda() {
  return "paginaajuda";
 }

}

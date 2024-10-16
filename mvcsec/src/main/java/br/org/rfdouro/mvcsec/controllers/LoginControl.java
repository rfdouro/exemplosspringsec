package br.org.rfdouro.mvcsec.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping
public class LoginControl {

 @Autowired
 HttpServletRequest request;

 @GetMapping("/login")
 public String login() {
  return "login";
 }
}

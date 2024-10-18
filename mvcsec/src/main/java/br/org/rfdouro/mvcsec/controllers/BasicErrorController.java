package br.org.rfdouro.mvcsec.controllers;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//controller que recebe os erros e mostra uma página customizada
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {

  public BasicErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes);
  }

  // mapeia os erros e encaminha para a página que mostra os mesmos 
  @GetMapping
  public String errorHtml(Model model, HttpServletRequest request, HttpServletResponse response) {
    HttpStatus status = getStatus(request);
    Map<String, Object> errorAttributes = getErrorAttributes(request,
        ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.values()));
    response.setStatus(status.value());
    model.addAllAttributes(errorAttributes);
    return "paginaerro";
  }
}

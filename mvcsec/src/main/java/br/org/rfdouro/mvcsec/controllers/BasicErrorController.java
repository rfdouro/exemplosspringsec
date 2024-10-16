package br.org.rfdouro.mvcsec.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {

 public BasicErrorController(ErrorAttributes errorAttributes) {
  super(errorAttributes);
 }

 @GetMapping
 public String errorHtml(Model model, HttpServletRequest request, HttpServletResponse response) {
  HttpStatus status = getStatus(request);
  /*
   * Map<String, Object> model = Collections
   * .unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request,
   * MediaType.TEXT_HTML)));
   */
  Map<String, Object> errorAttributes = getErrorAttributes(request,
    ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.values()));
  // String message = getMessage(status, errorAttributes);
  // String path = (String) errorAttributes.get("path");
  response.setStatus(status.value());
  // ModelAndView modelAndView = resolveErrorView(request, response, status,
  // errorAttributes);
  model.addAllAttributes(errorAttributes);
  // return (modelAndView != null) ? modelAndView : new ModelAndView("error",
  // errorAttributes);
  return "paginaerro";
 }
}

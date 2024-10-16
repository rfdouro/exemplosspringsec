package br.org.rfdouro.apisec.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.rfdouro.apisec.models.AlunoRepository;
import br.org.rfdouro.apisec.models.Tarefa;
import br.org.rfdouro.apisec.models.TarefaRepository;
import jakarta.servlet.ServletContext;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tarefas")
public class TarefasControl {
 @Autowired
 private TarefaRepository tarefaRepository;
 @Autowired
 private AlunoRepository alunoRepository;
 @Autowired
 ServletContext context;

 @GetMapping
 public ResponseEntity<List<Tarefa>> todasTarefas() {
  return ResponseEntity.ok().body(tarefaRepository.findAll(Sort.by("descricao")));
 }

 @GetMapping("/{id}")
 public ResponseEntity<Tarefa> umaTarefa(@PathVariable(value = "id") Long id) {
  return ResponseEntity.status(200).body(tarefaRepository.findById(id).get());
 }

 @PostMapping
 public ResponseEntity<String> salvaTarefa(@RequestBody Tarefa tarefa) {
  tarefa.setAluno(alunoRepository.findById(tarefa.getAluno().getId()).get());
  tarefa = tarefaRepository.save(tarefa);
  return ResponseEntity.status(201).body(context.getContextPath() + "/tarefas/" + tarefa.getId());
 }

 @PutMapping("/{id}")
 public ResponseEntity<String> atualizaTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
  Tarefa a = tarefaRepository.findById(id).get();
  if (a != null || a == null) {
   tarefa.setAluno(alunoRepository.findById(tarefa.getAluno().getId()).get());
   tarefa = tarefaRepository.save(tarefa);
  }
  return ResponseEntity.status(200).body(context.getContextPath() + "/tarefas/" + tarefa.getId());
 }

 @DeleteMapping("/{id}")
 public ResponseEntity<String> excluiTarefa(@PathVariable Long id) {
  Tarefa a = tarefaRepository.findById(id).get();
  if (a != null) {
   tarefaRepository.delete(a);
  }
  return ResponseEntity.status(200).build();
 }

}

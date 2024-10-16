package br.org.rfdouro.apisec.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.rfdouro.apisec.models.Aluno;
import br.org.rfdouro.apisec.models.AlunoRepository;
import br.org.rfdouro.apisec.models.Tarefa;
import jakarta.servlet.ServletContext;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/alunos")
public class AlunosControl {
 @Autowired
 private AlunoRepository alunoRepository;
 @Autowired
 ServletContext context;

 @GetMapping
 public ResponseEntity<List<Aluno>> todosAlunos() {
  return ResponseEntity.ok().body(alunoRepository.findAll(Sort.by("nome")));
 }

 @GetMapping("/{id}")
 public ResponseEntity<Aluno> umAluno(@PathVariable(value = "id") Long id) {
  return ResponseEntity.status(200).body(alunoRepository.findById(id).get());
 }

 @GetMapping("/{id}/tarefas")
 public ResponseEntity<List<Tarefa>> tarefasAluno(@PathVariable(value = "id") Long id) {
  return ResponseEntity.status(200).body(alunoRepository.findById(id).get().getTarefas());
 }

 @PostMapping
 public ResponseEntity<String> salvaAluno(@RequestBody Aluno aluno) {
  aluno = alunoRepository.save(aluno);
  return ResponseEntity.status(201).body(context.getContextPath() + "/alunos/" + aluno.getId());
 }

 @PutMapping("/{id}")
 public ResponseEntity<String> atualizaAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
  Aluno a = alunoRepository.findById(id).get();
  if (a != null || a == null) {
   aluno = alunoRepository.save(aluno);
  }
  return ResponseEntity.status(200).body(context.getContextPath() + "/alunos/" + aluno.getId());
 }

 @DeleteMapping("/{id}")
 public ResponseEntity<String> excluiAluno(@PathVariable Long id) {
  Aluno a = alunoRepository.findById(id).get();
  if (a != null) {
   alunoRepository.delete(a);
  }
  return ResponseEntity.status(200).build();
 }

}

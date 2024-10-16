package br.org.rfdouro.apisec.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class Aluno {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String nome;
 @OneToMany(mappedBy = "aluno")
 @OrderBy(value = "descricao ASC")
 @JsonIgnore
 private List<Tarefa> tarefas;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getNome() {
  return nome;
 }

 public void setNome(String nome) {
  this.nome = nome;
 }

 public List<Tarefa> getTarefas() {
  return tarefas;
 }

 public void setTarefas(List<Tarefa> tarefas) {
  this.tarefas = tarefas;
 }

}

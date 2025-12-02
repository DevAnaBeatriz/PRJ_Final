package com.example.modulo1.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;

    private Boolean ativo = true;

    public Pessoa() {}

    public Pessoa(String nome, LocalDate dtNascimento, Boolean ativo) {
        this.nome = nome;
        this.dtNascimento = dtNascimento;
        this.ativo = ativo;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDtNascimento() { return dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento) { this.dtNascimento = dtNascimento; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}

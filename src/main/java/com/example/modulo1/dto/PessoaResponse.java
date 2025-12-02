package com.example.modulo1.dto;

import java.time.LocalDate;

public class PessoaResponse {
    private Long id;
    private String nome;
    private LocalDate dtNascimento;
    private Boolean ativo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDtNascimento() { return dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento) { this.dtNascimento = dtNascimento; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}

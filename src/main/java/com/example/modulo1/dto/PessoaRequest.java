package com.example.modulo1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PessoaRequest {

    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @NotNull(message = "dtNascimento é obrigatório")
    private LocalDate dtNascimento;

    @NotNull
    private Boolean ativo = true;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDtNascimento() { return dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento) { this.dtNascimento = dtNascimento; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}

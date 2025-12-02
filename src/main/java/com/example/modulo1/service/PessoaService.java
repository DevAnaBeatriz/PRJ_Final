package com.example.modulo1.service;

import com.example.modulo1.dto.PessoaRequest;
import com.example.modulo1.dto.PessoaResponse;
import com.example.modulo1.entity.Pessoa;
import com.example.modulo1.exception.ResourceNotFoundException;
import com.example.modulo1.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public PessoaResponse create(PessoaRequest req) {
        Pessoa p = new Pessoa(req.getNome(), req.getDtNascimento(), req.getAtivo());
        Pessoa saved = repository.save(p);
        return toResponse(saved);
    }

    public Page<PessoaResponse> list(Pageable pageable) {
        return repository.findByAtivoTrue(pageable).map(this::toResponse);
    }

    public PessoaResponse get(Long id) {
        Pessoa p = repository.findById(id)
                .filter(pessoa -> Boolean.TRUE.equals(pessoa.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
        return toResponse(p);
    }

    public PessoaResponse update(Long id, PessoaRequest req) {
        Pessoa p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
        p.setNome(req.getNome());
        p.setDtNascimento(req.getDtNascimento());
        p.setAtivo(req.getAtivo());
        Pessoa saved = repository.save(p);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pessoa não encontrada");
        }
        repository.deleteById(id);
    }

    private PessoaResponse toResponse(Pessoa p) {
        PessoaResponse r = new PessoaResponse();
        r.setId(p.getId());
        r.setNome(p.getNome());
        r.setDtNascimento(p.getDtNascimento());
        r.setAtivo(p.getAtivo());
        return r;
    }
}

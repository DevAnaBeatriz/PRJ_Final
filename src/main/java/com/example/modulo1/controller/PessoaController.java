package com.example.modulo1.controller;

import com.example.modulo1.dto.PessoaRequest;
import com.example.modulo1.dto.PessoaResponse;
import com.example.modulo1.service.PessoaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private static final Logger log = LoggerFactory.getLogger(PessoaController.class);

    @Autowired
    private PessoaService service;

    @PostMapping
    public ResponseEntity<PessoaResponse> create(@Valid @RequestBody PessoaRequest req) {
        log.info("Recebida requisição POST para criar pessoa: nome={}, dtNascimento={}, ativo={}",
                req.getNome(), req.getDtNascimento(), req.getAtivo());

        PessoaResponse created = service.create(req);

        log.info("Pessoa criada com sucesso: id={}", created.getId());

        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<PessoaResponse>> list(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        log.info("Recebida requisição GET /api/pessoas?page={}&size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<PessoaResponse> page = service.list(pageable);

        log.info("Listagem retornada: totalElements={}, totalPages={}",
                page.getTotalElements(), page.getTotalPages());

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> get(@PathVariable Long id) {
        log.info("Recebida requisição GET /api/pessoas/{}", id);

        PessoaResponse resp = service.get(id);

        log.info("Pessoa encontrada: id={}, nome={}", resp.getId(), resp.getNome());

        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody PessoaRequest req) {

        log.info("Recebida requisição PUT para atualizar pessoa {}: nome={}, dtNascimento={}, ativo={}",
                id, req.getNome(), req.getDtNascimento(), req.getAtivo());

        PessoaResponse updated = service.update(id, req);

        log.info("Pessoa atualizada com sucesso: id={}", updated.getId());

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Recebida requisição DELETE para deletar pessoa {}", id);

        service.delete(id);

        log.info("Pessoa {} deletada com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}

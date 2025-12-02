package com.example.modulo1.repository;

import com.example.modulo1.entity.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Page<Pessoa> findByAtivoTrue(Pageable pageable);
}

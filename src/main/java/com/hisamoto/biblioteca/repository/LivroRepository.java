package com.hisamoto.biblioteca.repository;

import com.hisamoto.biblioteca.model.Livro;
import com.hisamoto.biblioteca.model.LivroStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByStatus(LivroStatus status);
}
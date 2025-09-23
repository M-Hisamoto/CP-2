package com.hisamoto.biblioteca.repository;

import com.hisamoto.biblioteca.model.Emprestimo;
import com.hisamoto.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByDataDevolucaoIsNull();
    boolean existsByLivroAndDataDevolucaoIsNull(Livro livro);
}
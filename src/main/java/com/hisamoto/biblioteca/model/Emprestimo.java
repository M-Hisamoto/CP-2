package com.hisamoto.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Livro vinculado
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id")
    @NotNull(message = "Livro é obrigatório.")
    private Livro livro;

    // Usuário vinculado
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @NotNull(message = "Usuário é obrigatório.")
    private Usuario usuario;

    @NotNull(message = "A data de retirada é obrigatória.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataRetirada;

    @NotNull(message = "A data prevista para devolução é obrigatória.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataPrevistaDevolucao;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataDevolucao;

    @AssertTrue(message = "Data prevista de devolução deve ser posterior à data de retirada.")
    public boolean isDataPrevistaPosterior() {
        if (dataRetirada == null || dataPrevistaDevolucao == null) return true;
        return dataPrevistaDevolucao.isAfter(dataRetirada);
    }

    public Emprestimo() {}

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDate getDataRetirada() { return dataRetirada; }
    public void setDataRetirada(LocalDate dataRetirada) { this.dataRetirada = dataRetirada; }

    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) { this.dataPrevistaDevolucao = dataPrevistaDevolucao; }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }
}
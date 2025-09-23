package com.hisamoto.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotBlank(message = "O autor é obrigatório.")
    private String autor;

    @NotNull(message = "O ano de publicação é obrigatório.")
    private Integer anoPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivroStatus status;

    public Livro() {}

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = LivroStatus.DISPONIVEL;
        }
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public LivroStatus getStatus() { return status; }
    public void setStatus(LivroStatus status) { this.status = status; }
}
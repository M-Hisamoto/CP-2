package com.hisamoto.biblioteca.controller;

import com.hisamoto.biblioteca.model.Livro;
import com.hisamoto.biblioteca.model.LivroStatus;
import com.hisamoto.biblioteca.repository.EmprestimoRepository;
import com.hisamoto.biblioteca.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LivroController(LivroRepository livroRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroRepository.findAll());
        return "livros/lista";
    }

    @GetMapping("/disponiveis")
    public String listarDisponiveis(Model model) {
        model.addAttribute("livros", livroRepository.findByStatus(LivroStatus.DISPONIVEL));
        model.addAttribute("apenasDisponiveis", true);
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("livro", new Livro());
        return "livros/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("livro") Livro livro,
                        BindingResult result,
                        RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "livros/form";
        }
        // status default via @PrePersist
        livroRepository.save(livro);
        ra.addFlashAttribute("sucesso", "Livro cadastrado com sucesso!");
        return "redirect:/livros";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        var livro = livroRepository.findById(id).orElse(null);
        if (livro == null) {
            ra.addFlashAttribute("erro", "Livro não encontrado.");
            return "redirect:/livros";
        }
        model.addAttribute("livro", livro);
        return "livros/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("livro") Livro livro,
                            BindingResult result,
                            RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "livros/form";
        }
        livro.setId(id);
        // Não permitir alteração direta de status aqui via form
        var existente = livroRepository.findById(id).orElse(null);
        if (existente != null) {
            livro.setStatus(existente.getStatus());
        }
        livroRepository.save(livro);
        ra.addFlashAttribute("sucesso", "Livro atualizado com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        var livro = livroRepository.findById(id).orElse(null);
        if (livro == null) {
            ra.addFlashAttribute("erro", "Livro não encontrado.");
            return "redirect:/livros";
        }
        // Impede excluir livro com empréstimo ativo
        boolean emprestadoAtivo = emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro);
        if (emprestadoAtivo || livro.getStatus() == LivroStatus.EMPRESTADO) {
            ra.addFlashAttribute("erro", "Não é possível excluir um livro com empréstimo ativo.");
            return "redirect:/livros";
        }
        livroRepository.deleteById(id);
        ra.addFlashAttribute("sucesso", "Livro excluído com sucesso!");
        return "redirect:/livros";
    }
}
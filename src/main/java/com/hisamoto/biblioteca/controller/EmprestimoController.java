package com.hisamoto.biblioteca.controller;

import com.hisamoto.biblioteca.model.Emprestimo;
import com.hisamoto.biblioteca.model.Livro;
import com.hisamoto.biblioteca.model.LivroStatus;
import com.hisamoto.biblioteca.repository.EmprestimoRepository;
import com.hisamoto.biblioteca.repository.LivroRepository;
import com.hisamoto.biblioteca.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoController(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimosAtivos", emprestimoRepository.findByDataDevolucaoIsNull());
        model.addAttribute("todosEmprestimos", emprestimoRepository.findAll());
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("livrosDisponiveis", livroRepository.findByStatus(LivroStatus.DISPONIVEL));
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "emprestimos/form";
    }

    @PostMapping
    @Transactional
    public String criar(@Valid @ModelAttribute("emprestimo") Emprestimo emprestimo,
                        BindingResult result,
                        Model model,
                        RedirectAttributes ra) {
        // Validar livro e usuário selecionados (ids já vêm populados via binding do objeto)
        if (emprestimo.getLivro() == null || emprestimo.getLivro().getId() == null) {
            result.rejectValue("livro", "livro.obrigatorio", "Selecione um livro.");
        }
        if (emprestimo.getUsuario() == null || emprestimo.getUsuario().getId() == null) {
            result.rejectValue("usuario", "usuario.obrigatorio", "Selecione um usuário.");
        }

        Livro livroPersistido = null;
        if (emprestimo.getLivro() != null && emprestimo.getLivro().getId() != null) {
            livroPersistido = livroRepository.findById(emprestimo.getLivro().getId()).orElse(null);
            if (livroPersistido == null) {
                result.rejectValue("livro", "livro.invalido", "Livro inválido.");
            } else if (livroPersistido.getStatus() == LivroStatus.EMPRESTADO) {
                result.rejectValue("livro", "livro.indisponivel", "Livro está emprestado.");
            }
        }

        if (emprestimo.getUsuario() != null && emprestimo.getUsuario().getId() != null) {
            var usuarioOk = usuarioRepository.findById(emprestimo.getUsuario().getId()).orElse(null);
            if (usuarioOk == null) {
                result.rejectValue("usuario", "usuario.invalido", "Usuário inválido.");
            } else {
                emprestimo.setUsuario(usuarioOk);
            }
        }

        // Ajuste o relacionamento do livro no objeto emprestimo para o managed entity
        if (livroPersistido != null) {
            emprestimo.setLivro(livroPersistido);
        }

        if (result.hasErrors()) {
            model.addAttribute("livrosDisponiveis", livroRepository.findByStatus(LivroStatus.DISPONIVEL));
            model.addAttribute("usuarios", usuarioRepository.findAll());
            return "emprestimos/form";
        }

        // Salvar empréstimo e marcar livro como emprestado
        emprestimoRepository.save(emprestimo);
        livroPersistido.setStatus(LivroStatus.EMPRESTADO);
        livroRepository.save(livroPersistido);

        ra.addFlashAttribute("sucesso", "Empréstimo registrado com sucesso!");
        return "redirect:/emprestimos";
    }

    @PostMapping("/{id}/devolver")
    @Transactional
    public String devolver(@PathVariable Long id, RedirectAttributes ra) {
        var emp = emprestimoRepository.findById(id).orElse(null);
        if (emp == null) {
            ra.addFlashAttribute("erro", "Empréstimo não encontrado.");
            return "redirect:/emprestimos";
        }
        if (emp.getDataDevolucao() != null) {
            ra.addFlashAttribute("erro", "Este empréstimo já foi finalizado.");
            return "redirect:/emprestimos";
        }
        emp.setDataDevolucao(LocalDate.now());
        emprestimoRepository.save(emp);

        var livro = emp.getLivro();
        livro.setStatus(LivroStatus.DISPONIVEL);
        livroRepository.save(livro);

        ra.addFlashAttribute("sucesso", "Livro devolvido com sucesso!");
        return "redirect:/emprestimos";
    }
}
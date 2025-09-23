package com.hisamoto.biblioteca.controller;

import com.hisamoto.biblioteca.model.Usuario;
import com.hisamoto.biblioteca.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("usuario") Usuario usuario,
                        BindingResult result,
                        RedirectAttributes ra) {
        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "email.existente", "E-mail já cadastrado.");
        }

        if (result.hasErrors()) {
            return "usuarios/form";
        }
        try {
            usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "email.duplicado", "E-mail já cadastrado.");
            return "usuarios/form";
        }
        ra.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
        return "redirect:/usuarios";
    }
}
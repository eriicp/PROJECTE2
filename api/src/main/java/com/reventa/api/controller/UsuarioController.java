package com.reventa.api.controller;

import com.reventa.api.dto.UsuarioDTO;
import com.reventa.api.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        
        List<UsuarioDTO> listaSegura = usuarioRepository.findAll().stream()
            .map(usuario -> new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getDniNie(),
                usuario.getEstadoVerificacion(),
                usuario.getReputacionMedia(),
                usuario.getTotalResenas(),
                usuario.getFechaRegistro()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(listaSegura);
    }
}
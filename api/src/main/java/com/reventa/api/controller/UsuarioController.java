package com.reventa.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reventa.api.dto.UsuarioDTO;
import com.reventa.api.dto.UsuarioPerfilDTO;
import com.reventa.api.model.Usuario;
import com.reventa.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

   private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPerfilDTO> obtenerPerfil(@PathVariable Long id) {
        
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        
        if (usuario == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404
        }

        UsuarioPerfilDTO perfilDTO;
        perfilDTO = new UsuarioPerfilDTO(
               usuario.getIdUsuario(),
               usuario.getNombreCompleto(),
               usuario.getEmail(),
               "verificado", 
               4.8, // valores de prueba
               15   //valores de prueba 
       );

        return ResponseEntity.ok(perfilDTO); // Devuelve 200 OK con los datos
    }
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
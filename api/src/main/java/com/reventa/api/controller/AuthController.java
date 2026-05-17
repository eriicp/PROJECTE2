package com.reventa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reventa.api.dto.AuthResponseDTO;
import com.reventa.api.dto.LoginRequestDTO;
import com.reventa.api.dto.RegisterRequest;
import com.reventa.api.model.Usuario;
import com.reventa.api.model.enums.EstadoVerificacion;
import com.reventa.api.repository.UsuarioRepository;
import com.reventa.api.security.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Encripta la contraseña antes de guardarla

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        String tokenGenerado = jwtUtils.generateJwtToken(request.getEmail()); 
        
        //Buscamos al usuario en la BBDD para sacar su ID
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Devolvemos el DTO con ambas cosas
        AuthResponseDTO respuesta = new AuthResponseDTO(tokenGenerado, usuario.getIdUsuario());
        
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest signUpRequest) {
        // 1. Verificamos si el email ya existe
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: ¡El email ya está en uso!");
        }

        // 2. Verificamos si el DNI ya existe
        if (usuarioRepository.existsByDniNie(signUpRequest.getDniNie())) {
            return ResponseEntity.badRequest().body("Error: ¡El DNI/NIE ya está registrado!");
        }

        // 3. Creamos el nuevo usuario con los datos del DTO
        Usuario user = new Usuario();
        user.setNombreCompleto(signUpRequest.getNombreCompleto());
        user.setEmail(signUpRequest.getEmail());
        user.setDniNie(signUpRequest.getDniNie());
        
        // Encriptamos la contraseña con BCrypt
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        
        // Asignamos el estado inicial
        user.setEstadoVerificacion(EstadoVerificacion.pendiente);

        // Los campos reputacionMedia y totalResenas ya tienen valores por defecto en tu clase Usuario

        // 4. Guardamos en la base de datos MySQL
        usuarioRepository.save(user);

        return ResponseEntity.ok("¡Usuario registrado con éxito!");
    }
}
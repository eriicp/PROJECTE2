package com.reventa.api.controller;

import com.reventa.api.dto.JwtResponse;
import com.reventa.api.dto.LoginRequest;
import com.reventa.api.dto.RegisterRequest;
import com.reventa.api.model.Usuario;
import com.reventa.api.model.enums.EstadoVerificacion;
import com.reventa.api.repository.UsuarioRepository;
import com.reventa.api.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Encripta la contraseña antes de guardarla

    @Autowired
    private JwtUtils jwtUtils;

    // --- ENDPOINT DE LOGIN (El que ya teníamos) ---
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    // --- NUEVO ENDPOINT DE REGISTRO ---
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
        
        // ¡Súper importante! Encriptamos la contraseña con BCrypt
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        
        // Asignamos el estado inicial
        user.setEstadoVerificacion(EstadoVerificacion.pendiente);

        // Los campos reputacionMedia y totalResenas ya tienen valores por defecto en tu clase Usuario

        // 4. Guardamos en la base de datos MySQL
        usuarioRepository.save(user);

        return ResponseEntity.ok("¡Usuario registrado con éxito!");
    }
}
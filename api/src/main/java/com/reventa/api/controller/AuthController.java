package com.reventa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.reventa.api.dto.AuthResponseDTO;
import com.reventa.api.dto.LoginRequestDTO;
import com.reventa.api.model.Usuario;
import com.reventa.api.model.enums.EstadoVerificacion;
import com.reventa.api.repository.UsuarioRepository;
import com.reventa.api.security.JwtUtils;
import com.reventa.api.service.S3Service;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private S3Service s3Service;

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

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> registerUser(
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("dniNie") String dniNie,
            @RequestPart("archivoDni") org.springframework.web.multipart.MultipartFile archivoDni) {
        
        if (usuarioRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Error: ¡El email ya está en uso!");
        }
        if (usuarioRepository.existsByDniNie(dniNie)) {
            return ResponseEntity.badRequest().body("Error: ¡El DNI/NIE ya está registrado!");
        }

        try {
            // Subimos el archivo a S3 y obtenemos la URL
            String urlDni = s3Service.subirDni(archivoDni);

            Usuario user = new Usuario();
            user.setNombreCompleto(nombreCompleto);
            user.setEmail(email);
            user.setDniNie(dniNie);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setEstadoVerificacion(EstadoVerificacion.pendiente);
            user.setUrlDni(urlDni); // Guardamos la URL en base de datos

            usuarioRepository.save(user);
            return ResponseEntity.ok("¡Usuario registrado con éxito!");
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al subir el DNI o registrar usuario: " + e.getMessage());
        }
    }
}
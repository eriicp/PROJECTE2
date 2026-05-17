package com.reventa.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reventa.api.model.Entrada;
import com.reventa.api.model.Usuario;
import com.reventa.api.repository.EntradaRepository;
import com.reventa.api.repository.UsuarioRepository;
import com.reventa.api.service.StripeService;

@RestController
@RequestMapping("/api/pagos")
public class StripeController {

    private final StripeService stripeService;
    private final EntradaRepository entradaRepository;
    private final UsuarioRepository usuarioRepository;

    public StripeController(StripeService stripeService, EntradaRepository entradaRepository, UsuarioRepository usuarioRepository) {
        this.stripeService = stripeService;
        this.entradaRepository = entradaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Petición que mandará Android: { "idEntrada": 1, "idComprador": 5 }
    @PostMapping("/comprar")
    public ResponseEntity<?> iniciarCompra(@RequestBody Map<String, Long> request) {
        try {
            Long idEntrada = request.get("idEntrada");
            Long idComprador = request.get("idComprador");

            // 1. Buscamos los datos reales en tu BBDD
            Entrada entrada = entradaRepository.findById(idEntrada)
                    .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));
            
            Usuario comprador = usuarioRepository.findById(idComprador)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 2. Llamamos a la magia de Stripe
            Map<String, String> respuestaStripe = stripeService.procesarCompra(entrada, comprador);

            // 3. Devolvemos el secreto a Android
            return ResponseEntity.ok(respuestaStripe);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
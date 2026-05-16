package com.reventa.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reventa.api.service.StripeService;

@RestController
@RequestMapping("/api/pagos")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/crear-intento")
    public ResponseEntity<Map<String, String>> crearIntento(@RequestBody Map<String, Object> datos) {
        try {
            Long precio = Long.valueOf(datos.get("precio").toString());

            String clientSecret = stripeService.crearIntentoDePago(precio);

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("clientSecret", clientSecret);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
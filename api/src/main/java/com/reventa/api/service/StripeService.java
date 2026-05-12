package com.reventa.api.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.reventa.api.model.Entrada;
import com.reventa.api.model.EstadoPago;
import com.reventa.api.model.Transaccion;
import com.reventa.api.model.Usuario;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct; 

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // Esto se ejecuta nada más arrancar tu backend para darle la llave a Stripe
        Stripe.apiKey = secretKey;
    }

    public String crearIntentoDePago(Long cantidadCentimos) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(cantidadCentimos)
                .setCurrency("eur")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        
        return intent.getClientSecret();
    }

    @Transactional
    public Map<String, String> procesarCompra(Entrada entrada, Usuario comprador) throws Exception {
        // 1. Cálculos (Ejemplo: 10% de comisión)
        BigDecimal precioBase = entrada.getPrecioReventa();
        BigDecimal comision = precioBase.multiply(new BigDecimal("0.10"));
        BigDecimal total = precioBase.add(comision);

        // 2. Crear PaymentIntent en Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(total.multiply(new BigDecimal("100")).longValue()) // A céntimos
                .setCurrency("eur")
                .putMetadata("id_entrada", entrada.getIdEntrada().toString())
                .putMetadata("id_comprador", comprador.getIdUsuario().toString())
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        // 3. Guardar en nuestra DB como PENDIENTE
        Transaccion t = new Transaccion();
        t.setEntrada(entrada);
        t.setComprador(comprador);
        t.setMontoBase(precioBase);
        t.setComisionPlataforma(comision);
        t.setMontoTotal(total);
        t.setStripePaymentIntentId(intent.getId());
        t.setEstadoPago(EstadoPago.PENDIENTE);
        
        transaccionRepository.save(t);

        // Devolvemos el secreto para Android
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }
}
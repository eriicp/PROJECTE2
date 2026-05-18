package com.reventa.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reventa.api.model.Entrada;
import com.reventa.api.model.LiquidacionEscrow;
import com.reventa.api.model.Transaccion;
import com.reventa.api.model.Usuario;
import com.reventa.api.model.enums.EstadoEntrada;
import com.reventa.api.model.enums.EstadoFondos;
import com.reventa.api.model.enums.EstadoPago;
import com.reventa.api.repository.EntradaRepository;
import com.reventa.api.repository.LiquidacionEscrowRepository;
import com.reventa.api.repository.TransaccionRepository;
import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    private final TransaccionRepository transaccionRepository;
    private final LiquidacionEscrowRepository escrowRepository;
    private final EntradaRepository entradaRepository;

    // Inyección de dependencias por constructor
  public StripeService(TransaccionRepository transaccionRepository, 
                     LiquidacionEscrowRepository escrowRepository,
                     EntradaRepository entradaRepository) { 
    this.transaccionRepository = transaccionRepository;
    this.escrowRepository = escrowRepository;
    this.entradaRepository = entradaRepository; 
    }

    @PostConstruct
    public void init() {
        // Inicializamos la librería de Stripe con tu clave secreta
        Stripe.apiKey = secretKey;
    }

    /**
     * PASO 1: Android pide comprar una entrada.
     * Creamos el intento de pago en Stripe y guardamos la transacción como PENDIENTE.
     */
    @Transactional
    public Map<String, String> procesarCompra(Entrada entrada, Usuario comprador) throws Exception {
        if (entrada.getEstado() != EstadoEntrada.disponible) {
            throw new RuntimeException("Lo sentimos, esta entrada ya ha sido vendida o está reservada.");
        }

        // Bloqueamos la entrada temporalmente
        entrada.setEstado(EstadoEntrada.vendida);
        entradaRepository.save(entrada);
        
        // 1. Cálculos de comisiones (Ejemplo: 10% de comisión para la plataforma)
        BigDecimal precioBase = entrada.getPrecioReventa();
        BigDecimal comision = precioBase.multiply(new BigDecimal("0.10"));
        BigDecimal total = precioBase.add(comision);

        // 2. Crear PaymentIntent en Stripe (Stripe siempre trabaja en céntimos)
        long cantidadEnCentimos = total.multiply(new BigDecimal("100")).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(cantidadEnCentimos)
                .setCurrency("eur")
                // Los metadatos son útiles por si luego miras el panel web de Stripe
                .putMetadata("id_entrada", entrada.getIdEntrada().toString())
                .putMetadata("id_comprador", comprador.getIdUsuario().toString())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        // 3. Guardar en nuestra base de datos como PENDIENTE
        Transaccion t = new Transaccion();
        t.setEntrada(entrada);
        t.setComprador(comprador);
        t.setMontoBase(precioBase);
        t.setComisionPlataforma(comision);
        t.setMontoTotal(total);
        t.setStripePaymentIntentId(intent.getId());
        t.setEstadoPago(EstadoPago.PENDIENTE);
        t.setFechaTransaccion(LocalDateTime.now());
        
        transaccionRepository.save(t);

        // 4. Devolvemos el "Client Secret" que Android necesita para abrir la ventanita de pago
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }

    /**
     * PASO 2: El Webhook de Stripe nos avisa de que el cliente ha pagado con éxito.
     * Pasamos la transacción a COMPLETADO y enviamos los fondos al ESCROW.
     */
    @Transactional
    public void procesarPagoExitoso(String stripePaymentIntentId) {
        // 1. Buscamos la transacción PENDIENTE usando el ID de Stripe
        Transaccion transaccion = transaccionRepository.findByStripePaymentIntentId(stripePaymentIntentId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada para el Intent: " + stripePaymentIntentId));

        // 2. Comprobación de seguridad: Si ya estaba completada, no hacemos nada duplicado
        if (transaccion.getEstadoPago() == EstadoPago.COMPLETADO) {
            return;
        }

        // 3. Marcamos la compra como COMPLETADA
        transaccion.setEstadoPago(EstadoPago.COMPLETADO);
        transaccionRepository.save(transaccion);

        Entrada entrada = transaccion.getEntrada();
        entrada.setEstado(EstadoEntrada.vendida);
        entradaRepository.save(entrada);

        // 4. Magia del Escrow: Creamos la retención de los fondos
        LiquidacionEscrow escrow = new LiquidacionEscrow();
        escrow.setTransaccion(transaccion);
        
        // El dinero se liberará 48 horas después de la fecha del evento
        LocalDateTime fechaEvento = transaccion.getEntrada().getEvento().getFechaEvento();
        escrow.setFechaLiberacionPrevista(fechaEvento.plusHours(48));
        
        escrow.setEstadoFondos(EstadoFondos.RETENIDO);
        
        escrowRepository.save(escrow);
        
        System.out.println("✅ Transacción " + transaccion.getIdTransaccion() + " completada. Fondos retenidos en Escrow.");
    }

    // 1. Crea la cuenta vacía en Stripe
    public String crearCuentaConectada(String email) throws Exception {
        AccountCreateParams params = AccountCreateParams.builder()
            .setType(AccountCreateParams.Type.EXPRESS)
            .setEmail(email)
            .build();
            
        Account account = Account.create(params);
        return account.getId(); // Ej: acct_1Nxyz...
    }

    // 2. Genera el link para que el usuario rellene sus datos bancarios
    public String generarLinkOnboarding(String accountId) throws Exception {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
            .setAccount(accountId)
            .setRefreshUrl("https://tudominio.com/reauth") // URL si el link expira
            .setReturnUrl("https://tudominio.com/success") // URL cuando termina con éxito
            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
            .build();

        AccountLink accountLink = AccountLink.create(params);
        return accountLink.getUrl();
    }
}
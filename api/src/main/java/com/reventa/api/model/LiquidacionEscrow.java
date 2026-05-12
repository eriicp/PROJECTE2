package com.reventa.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "liquidaciones_escrow")
public class LiquidacionEscrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEscrow;

    @OneToOne
    @JoinColumn(name = "id_transaccion")
    private Transaccion transaccion;

    private LocalDateTime fechaLiberacionPrevista;

    @Enumerated(EnumType.STRING)
    private EstadoFondos estadoFondos = EstadoFondos.RETENIDO;

    private String stripeTransferId;
    
    // Getters y Setters...
}

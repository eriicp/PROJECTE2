package com.reventa.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.reventa.api.model.enums.EstadoFondos;

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

    public Long getIdEscrow() {
        return idEscrow;
    }

    public void setIdEscrow(Long idEscrow) {
        this.idEscrow = idEscrow;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public LocalDateTime getFechaLiberacionPrevista() {
        return fechaLiberacionPrevista;
    }

    public void setFechaLiberacionPrevista(LocalDateTime fechaLiberacionPrevista) {
        this.fechaLiberacionPrevista = fechaLiberacionPrevista;
    }

    public EstadoFondos getEstadoFondos() {
        return estadoFondos;
    }

    public void setEstadoFondos(EstadoFondos estadoFondos) {
        this.estadoFondos = estadoFondos;
    }

    public String getStripeTransferId() {
        return stripeTransferId;
    }

    public void setStripeTransferId(String stripeTransferId) {
        this.stripeTransferId = stripeTransferId;
    }    
}
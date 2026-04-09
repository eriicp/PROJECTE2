package com.reventa.api.dto;

import java.math.BigDecimal;

import com.reventa.api.model.Entrada;
import com.reventa.api.model.enums.EstadoEntrada;

public class EntradaDTO {
    private Long idEntrada;
    private String nombreEvento;
    private String nombreVendedor;
    private BigDecimal precioReventa;
    private EstadoEntrada estado;
    private String tipoAsiento;

    public EntradaDTO(Entrada e) {
        this.idEntrada = e.getIdEntrada();
        this.nombreEvento = e.getEvento() != null ? e.getEvento().getNombre() : "Desconocido";
        this.nombreVendedor = e.getVendedor() != null ? e.getVendedor().getNombreCompleto() : "Desconocido";
        this.precioReventa = e.getPrecioReventa();
        this.estado = e.getEstado();
        this.tipoAsiento = e.getTipoAsiento();
    }

}
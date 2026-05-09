package com.reventa.api.dto;

import com.reventa.api.model.Evento;
import com.reventa.api.model.enums.CategoriaEvento;
import java.time.LocalDateTime;

public class EventoDTO {
    private Long idEvento;
    private String nombre;
    private LocalDateTime fechaEvento;
    private String ubicacion;
    private CategoriaEvento categoria;

    public EventoDTO(Evento e) {
        this.idEvento = e.getIdEvento();
        this.nombre = e.getNombre();
        this.fechaEvento = e.getFechaEvento();
        this.ubicacion = e.getUbicacion();
        this.categoria = e.getCategoria();
    }

    // Getters
    public Long getIdEvento() { return idEvento; }
    public String getNombre() { return nombre; }
    public LocalDateTime getFechaEvento() { return fechaEvento; }
    public String getUbicacion() { return ubicacion; }
    public CategoriaEvento getCategoria() { return categoria; }
}
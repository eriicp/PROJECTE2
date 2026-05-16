package com.reventa.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reventa.api.dto.EventoDTO;
import com.reventa.api.model.Evento;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.service.EventoService;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;


    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<Evento>> getEventosProximosSeisMeses() {

        List<Evento> proximosEventos = eventoService.obtenerProximosEventos();
        
        return ResponseEntity.ok(proximosEventos);
    }

    @GetMapping
    public ResponseEntity<List<EventoDTO>> getAllEventos() {
        return ResponseEntity.ok(eventoService.getAllEventos());
    }

    // Endpoint para coger eventos por CATEGORIA
    @GetMapping("/search/categoria")
    public ResponseEntity<List<EventoDTO>> getEventosByCategoria(@RequestParam("cat") CategoriaEvento categoria) {
        return ResponseEntity.ok(eventoService.getEventosByCategoria(categoria));
    }

    // GET: http://localhost:8081/api/eventos/search?nombre=rosa
    @GetMapping("/search")
    public ResponseEntity<List<EventoDTO>> searchByName(@RequestParam String nombre) {
        return ResponseEntity.ok(eventoService.searchEventosByName(nombre));
}
}
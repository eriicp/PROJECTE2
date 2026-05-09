package com.reventa.api.controller;

import com.reventa.api.dto.EventoDTO;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Endpoint para coger TODOS los eventos

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
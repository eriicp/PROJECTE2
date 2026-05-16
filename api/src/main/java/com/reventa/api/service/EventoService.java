package com.reventa.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.reventa.api.dto.EventoDTO;
import com.reventa.api.model.Evento;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.repository.EventoRepository; m

@Service
public class EventoService {
private final EventoRepository eventoRepository;

    // Inyección del repositorio
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

   public List<Evento> obtenerProximosEventos() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentroDeSeisMeses = ahora.plusMonths(6);
        
        // Llamamos al nuevo método con @Query
        return eventoRepository.buscarProximosEventos(ahora, dentroDeSeisMeses);
    }

    // Obtener todos los eventos
    public List<EventoDTO> getAllEventos() {
        return eventoRepository.findAll().stream()
                .map(EventoDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener eventos filtrados por categoría
    public List<EventoDTO> getEventosByCategoria(CategoriaEvento categoria) {
        return eventoRepository.findByCategoria(categoria).stream()
                .map(EventoDTO::new)
                .collect(Collectors.toList());
    }

    public List<EventoDTO> searchEventosByName(String query) {
    return eventoRepository.findByNombreContainingIgnoreCase(query)
            .stream()
            .map(EventoDTO::new)
            .collect(Collectors.toList());
    }
}
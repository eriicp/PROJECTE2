package com.reventa.api.service;

import com.reventa.api.dto.EventoDTO;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

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
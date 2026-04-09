package com.reventa.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.reventa.api.dto.EntradaDTO;
import com.reventa.api.model.Entrada;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.model.enums.EstadoEntrada;
import com.reventa.api.repository.EntradaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;

    public List<Entrada> getAllEntradas() {
        return entradaRepository.findAll();
    }

    public Optional<Entrada> getEntradaById(Long id) {
        return entradaRepository.findById(id);
    }

    public List<Entrada> getEntradaByCategoria(CategoriaEvento categoria) {
        return entradaRepository.findByCategoriaEvento(categoria);
    }

    public Entrada saveEntrada(Entrada entrada) {
        // Por defecto una entrada nueva está disponible si no se indica
        if(entrada.getEstado() == null) {
            entrada.setEstado(EstadoEntrada.disponible);
        }
        return entradaRepository.save(entrada);
    }

    public List<EntradaDTO> searchByPriceRange(BigDecimal priceMin, BigDecimal priceMax, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "precioReventa"));
        return entradaRepository.findDisponiblesByPriceRangeJPQL(priceMin, priceMax, pageable)
                .stream().map(EntradaDTO::new).collect(Collectors.toList());
    }

    public List<Entrada> getDestacadas() {
        return entradaRepository.findByEsDestacadaTrue();
    }
}
package com.reventa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reventa.api.dto.EntradaDTO;
import com.reventa.api.model.Entrada;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.service.EntradaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;

    @GetMapping
    public ResponseEntity<List<Entrada>> getAllEntradas() {
        return ResponseEntity.ok(entradaService.getAllEntradas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Entrada>> getEntradaById(@PathVariable Long id) {
        return ResponseEntity.ok(entradaService.getEntradaById(id));           
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Entrada>> getEntradaByCategoria(@PathVariable CategoriaEvento categoria) {
        return ResponseEntity.ok(entradaService.getEntradaByCategoria(categoria));           
    }

    @PostMapping
    public ResponseEntity<Entrada> createEntrada(@RequestBody Entrada nuevaEntrada) {
        nuevaEntrada.setIdEntrada(null);
        return new ResponseEntity<>(entradaService.saveEntrada(nuevaEntrada), HttpStatus.CREATED);
    }

    // (Rango precios)
    @GetMapping(value = "/search", params = {"priceMin", "priceMax", "limit"})
    public ResponseEntity<List<EntradaDTO>> searchByPriceRange(
            @RequestParam BigDecimal priceMin, @RequestParam BigDecimal priceMax, @RequestParam int limit) {
        return ResponseEntity.ok(entradaService.searchByPriceRange(priceMin, priceMax, limit));
    }

    // Paginación de la investigación
    @GetMapping("/destacadas")
    public ResponseEntity<List<Entrada>> getDestacadasPaginated() {
        return ResponseEntity.ok(entradaService.getDestacadas());
    }
}
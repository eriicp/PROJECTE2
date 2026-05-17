package com.reventa.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reventa.api.dto.EntradaDTO;
import com.reventa.api.model.Entrada;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.service.EntradaService;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;


    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<Entrada>> obtenerEntradasPorEvento(@PathVariable Long idEvento) {
        
        List<Entrada> entradas = entradaService.obtenerEntradasPorEvento(idEvento);
        return ResponseEntity.ok(entradas);
        
    }

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

   @PostMapping(value = "/vender", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ponerEnVenta(
            @RequestParam("idEvento") Long idEvento,
            @RequestParam("idVendedor") Long idVendedor,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("zona") String zona,
            @RequestParam("fila") String fila,
            @RequestParam("asiento") String asiento,
            @RequestParam(value = "pdf", required = false) MultipartFile pdfFile) {
        try {
            Entrada entradaGuardada = entradaService.publicarEntrada(
                    idEvento, idVendedor, precio, zona, fila, asiento, pdfFile
            );

            return ResponseEntity.ok(Map.of(
                "message", "¡Entrada guardada en la base de datos con éxito!",
                "idEntrada", entradaGuardada.getIdEntrada() 
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }

    @GetMapping("/mis-ventas/{idUsuario}")
    public ResponseEntity<?> obtenerMisVentas(@PathVariable Long idUsuario) {
        try {
            // Buscamos las entradas directamente usando el ID que nos manda Android
            List<Entrada> misEntradas = entradaService.findByVendedor_IdUsuario(idUsuario);
            return ResponseEntity.ok(misEntradas);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }
}
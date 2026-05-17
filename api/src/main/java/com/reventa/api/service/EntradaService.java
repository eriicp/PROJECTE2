package com.reventa.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.reventa.api.dto.EntradaDTO;
import com.reventa.api.model.Entrada;
import com.reventa.api.model.Evento;
import com.reventa.api.model.Usuario;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.model.enums.EstadoEntrada;
import com.reventa.api.repository.EntradaRepository;
import com.reventa.api.repository.EventoRepository;
import com.reventa.api.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final S3Service s3Service;

    // Inyección por constructor de todas las herramientas necesarias
    public EntradaService(EntradaRepository entradaRepository, 
                          EventoRepository eventoRepository, 
                          UsuarioRepository usuarioRepository, 
                          S3Service s3Service) {
        this.entradaRepository = entradaRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.s3Service = s3Service;
    }

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

  // Dentro de tu EntradaService.java

    public List<Entrada> obtenerEntradasPorEvento(Long idEvento) {
        return entradaRepository.findByEventoIdEventoAndEstado(idEvento, EstadoEntrada.disponible);
    }

// Añadimos los nuevos campos al método
    @Transactional
    public Entrada publicarEntrada(Long idEvento, Long idVendedor, BigDecimal precio, 
                                   String zona, String fila, String asiento, 
                                   MultipartFile pdfFile) throws Exception {
        
        // subimos al bucket
        String urlPdfS3 = "Sin PDF adjunto";
        if (pdfFile != null && !pdfFile.isEmpty()) {
            urlPdfS3 = s3Service.subirPdf(pdfFile);
        }

        Usuario vendedor = usuarioRepository.findById(idVendedor)
                .orElseThrow(() -> new IllegalArgumentException("El usuario vendedor no existe."));

        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("El evento seleccionado no existe."));

        Entrada nuevaEntrada = new Entrada();
        nuevaEntrada.setEvento(evento);
        nuevaEntrada.setVendedor(vendedor);
        nuevaEntrada.setPrecioReventa(precio);
        
        // Nuevos campos de ubicación
        nuevaEntrada.setTipoAsiento(zona);
        nuevaEntrada.setFila(fila);
        nuevaEntrada.setAsiento(asiento);
        
        // futura implementacion para tener pdfs de entrada funcionando correctamente
        // nuevaEntrada.setUrlPdf(urlPdfS3); 

        // La marcamos como disponible para que salga en la app
        nuevaEntrada.setEstado(EstadoEntrada.disponible); 

        // Guardamos y devolvemos la entrada real
        return entradaRepository.save(nuevaEntrada);
    }

    public List<Entrada> findByVendedor_IdUsuario(Long id) {
        return entradaRepository.buscarMisVentas(id);
    }
}
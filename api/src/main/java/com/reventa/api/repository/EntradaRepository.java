package com.reventa.api.repository;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reventa.api.model.Entrada;
import com.reventa.api.model.enums.CategoriaEvento;
import com.reventa.api.model.enums.EstadoEntrada;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    List<Entrada> findByEstado(EstadoEntrada estado);
    @Query("SELECT e FROM Entrada e WHERE e.evento.categoria = :categoria AND e.estado = 'disponible'")
    List<Entrada> findByCategoriaEvento(CategoriaEvento categoria);

    
    // JPQL para buscar entradas por rango de precio 
    @Query("SELECT e FROM Entrada e WHERE e.precioReventa >= :precioMin AND e.precioReventa <= :precioMax AND e.estado = 'disponible'")
    List<Entrada> findDisponiblesByPriceRangeJPQL(@Param("precioMin") BigDecimal precioMin, @Param("precioMax") BigDecimal precioMax, Pageable pageable);

    // Entradas destacadas
    List<Entrada> findByEsDestacadaTrue();

    List<Entrada> findByEventoIdEvento(Long idEvento);

    List<Entrada> findByEventoIdEventoAndEstado(Long idEvento, EstadoEntrada estado);

    @Query("SELECT e FROM Entrada e WHERE e.vendedor.idUsuario = :idUsuario")
    List<Entrada> buscarMisVentas(@Param("idUsuario") Long idUsuario);
}
package com.reventa.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reventa.api.model.Evento;
import com.reventa.api.model.enums.CategoriaEvento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    List<Evento> findByCategoria(CategoriaEvento categoria);
    List<Evento> findByNombreContainingIgnoreCase(String nombre);
    @Query("SELECT e FROM Evento e WHERE e.fechaEvento BETWEEN :inicio AND :fin ORDER BY e.fechaEvento ASC")
    List<Evento> buscarProximosEventos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
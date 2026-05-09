package com.reventa.api.repository;

import com.reventa.api.model.Evento;
import com.reventa.api.model.enums.CategoriaEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    List<Evento> findByCategoria(CategoriaEvento categoria);
    List<Evento> findByNombreContainingIgnoreCase(String nombre);
}
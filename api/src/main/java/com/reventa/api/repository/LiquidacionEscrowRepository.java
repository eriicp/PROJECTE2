package com.reventa.api.repository;

import com.reventa.api.model.LiquidacionEscrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidacionEscrowRepository extends JpaRepository<LiquidacionEscrow, Long> {
}
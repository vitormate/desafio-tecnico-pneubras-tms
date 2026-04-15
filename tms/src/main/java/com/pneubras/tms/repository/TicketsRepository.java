package com.pneubras.tms.repository;

import com.pneubras.tms.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketsRepository extends JpaRepository<Tickets, Long> {
}

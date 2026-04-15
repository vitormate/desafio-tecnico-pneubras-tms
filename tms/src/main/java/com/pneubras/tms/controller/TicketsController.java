package com.pneubras.tms.controller;

import com.pneubras.tms.dto.request.CreateTicketsRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.service.TicketsService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketsController {

    private final TicketsService ticketsService;

    public TicketsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @PostMapping("new")
    @Transactional
    public ResponseEntity<TicketsResponse> createTickets(@RequestBody CreateTicketsRequest data, UriComponentsBuilder uriBuilder) {
        return ticketsService.createTickets(data, uriBuilder);
    }
}

package com.pneubras.tms.controller;

import com.pneubras.tms.dto.request.CreateTicketsRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.service.TicketsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity<PagedModel<TicketsResponse>> getAllTickets(@PageableDefault(sort = {"id"}, size = 30, page = 0) Pageable pageable) {
        return ticketsService.getAll(pageable);
    }
}

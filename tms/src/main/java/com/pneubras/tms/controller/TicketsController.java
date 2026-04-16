package com.pneubras.tms.controller;

import com.pneubras.tms.dto.request.AsignTicketRequest;
import com.pneubras.tms.dto.request.CreateTicketsRequest;
import com.pneubras.tms.dto.request.ReturnTicketRequest;
import com.pneubras.tms.dto.request.UpdateTicketRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.service.TicketsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
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
    public ResponseEntity<TicketsResponse> createTickets(@RequestBody @Valid CreateTicketsRequest data, UriComponentsBuilder uriBuilder) {
        return ticketsService.createTickets(data, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<PagedModel<TicketsResponse>> getAllTickets(@PageableDefault(sort = {"id"}, size = 30, page = 0) Pageable pageable) {
        return ticketsService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketsResponse> getTicketById(@PathVariable Long id) {
        return ticketsService.getTicketById(id);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<TicketsResponse> updateTicket(@PathVariable Long id, @RequestBody UpdateTicketRequest data) {
        return ticketsService.updateTicket(id, data);
    }

    @PutMapping("/asign/{id}")
    @Transactional
    public ResponseEntity<TicketsResponse> asignTicket(@PathVariable Long id, @RequestBody AsignTicketRequest data) throws BadRequestException {
        return ticketsService.asignTicket(id, data);
    }

    @PutMapping("/resolve/{id}")
    @Transactional
    public ResponseEntity<TicketsResponse> resolveTicket(@PathVariable Long id) throws BadRequestException {
        return ticketsService.resolveTicket(id);
    }

    @PutMapping("/close/{id}")
    @Transactional
    public ResponseEntity<TicketsResponse> closeTicket(@PathVariable Long id) throws BadRequestException {
        return ticketsService.closeTicket(id);
    }

    @PutMapping("/return/{id}")
    @Transactional
    public ResponseEntity<TicketsResponse> returnTicket(@PathVariable Long id, ReturnTicketRequest data) throws BadRequestException {
        return ticketsService.returnTicket(id, data);
    }
}

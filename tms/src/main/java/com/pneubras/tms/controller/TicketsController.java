package com.pneubras.tms.controller;

import com.pneubras.tms.dto.request.*;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.service.TicketsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketsController {

    private final TicketsService ticketsService;

    public TicketsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<TicketsResponse> createTickets(@RequestBody @Valid CreateTicketsRequest data, UriComponentsBuilder uriBuilder) {
        TicketsResponse dto = ticketsService.createTickets(data);
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<PagedModel<TicketsResponse>> getAllTickets(@PageableDefault(sort = {"id"}, size = 30, page = 0) Pageable pageable) {
        PagedModel<TicketsResponse> dto = ticketsService.getAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketsResponse> getTicketById(@PathVariable Long id) {
        TicketsResponse dto = ticketsService.getTicketById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<TicketsResponse> updateTicket(@PathVariable Long id, @RequestBody UpdateTicketRequest data) {
        TicketsResponse dto = ticketsService.updateTicket(id, data);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('AGENT')")
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TicketsResponse> assignTicket(@PathVariable Long id, @RequestBody @Valid AssignTicketRequest data) {
        TicketsResponse dto = ticketsService.assignTicket(id, data);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('AGENT')")
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<TicketsResponse> resolveTicket(@PathVariable Long id) {
        TicketsResponse dto = ticketsService.resolveTicket(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/close")
    public ResponseEntity<TicketsResponse> closeTicket(@PathVariable Long id) {
        TicketsResponse dto = ticketsService.closeTicket(id);
        return ResponseEntity.ok(dto);
    }

}

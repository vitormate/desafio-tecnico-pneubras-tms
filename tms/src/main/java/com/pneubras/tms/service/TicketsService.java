package com.pneubras.tms.service;

import com.pneubras.tms.dto.request.AsignTicketRequest;
import com.pneubras.tms.dto.request.CreateTicketsRequest;
import com.pneubras.tms.dto.request.UpdateTicketRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.entity.User;
import com.pneubras.tms.repository.TicketsRepository;
import com.pneubras.tms.repository.UserRepository;
import com.pneubras.tms.utils.enums.StatusEnum;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.pneubras.tms.entity.Tickets;

import java.time.LocalDateTime;

@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;
    private final UserRepository userRepository;

    public TicketsService(TicketsRepository ticketsRepository, UserRepository userRepository) {
        this.ticketsRepository = ticketsRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<TicketsResponse> createTickets(CreateTicketsRequest data, UriComponentsBuilder uriBuilder) {
        User createdBy = userRepository.findByLogin(data.login()).orElseThrow(
                () -> new EntityNotFoundException("User not found with login: " + data.login())
        );

        int hours = Tickets.checkDueHour(data.priority());
        Tickets ticket = new Tickets(data.title(), data.description(), data.priority(), hours, createdBy);
        ticketsRepository.save(ticket);
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
        return ResponseEntity.created(uri).body(new TicketsResponse(ticket));
    }

    public ResponseEntity<PagedModel<TicketsResponse>> getAll(Pageable pageable) {
        PagedModel<TicketsResponse> tickets = new PagedModel<>(ticketsRepository.findAll(pageable).map(TicketsResponse::new));
        return ResponseEntity.ok(tickets);
    }

    public ResponseEntity<TicketsResponse> getTicketById(Long id) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        return ResponseEntity.ok(new TicketsResponse(ticket));
    }

    public ResponseEntity<TicketsResponse> updateTicket(Long id, UpdateTicketRequest data) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        ticket.updateTicket(data.title(), data.description());

        return ResponseEntity.ok(new TicketsResponse(ticket));
    }

    public ResponseEntity<TicketsResponse> asignTicket(Long id, AsignTicketRequest data) throws BadRequestException {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        User agent = userRepository.findByLogin(data.login()).orElseThrow(
                () -> new EntityNotFoundException("User not found with login: " + data.login())
        );

        ticket.checkStatusAsign();
        ticket.setAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(StatusEnum.EM_PROGRESSO);

        return ResponseEntity.ok(new TicketsResponse(ticket));
    }

    public ResponseEntity<TicketsResponse> resolveTicket(Long id) throws BadRequestException {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        ticket.checkStatusResolve();
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(StatusEnum.RESOLVIDO);

        return ResponseEntity.ok(new TicketsResponse(ticket));
    }
}

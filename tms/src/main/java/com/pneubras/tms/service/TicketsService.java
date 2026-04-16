package com.pneubras.tms.service;

import com.pneubras.tms.dto.request.AssignTicketRequest;
import com.pneubras.tms.dto.request.CreateTicketsRequest;
import com.pneubras.tms.dto.request.ReturnTicketRequest;
import com.pneubras.tms.dto.request.UpdateTicketRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.entity.Tickets;
import com.pneubras.tms.entity.User;
import com.pneubras.tms.repository.TicketsRepository;
import com.pneubras.tms.repository.UserRepository;
import com.pneubras.tms.utils.enums.StatusEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;
    private final UserRepository userRepository;

    public TicketsService(TicketsRepository ticketsRepository, UserRepository userRepository) {
        this.ticketsRepository = ticketsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TicketsResponse createTickets(CreateTicketsRequest data) {
        User createdBy = userRepository.findByLogin(data.login()).orElseThrow(
                () -> new EntityNotFoundException("User not found with login: " + data.login())
        );

        int hours = Tickets.checkDueHour(data.priority());
        Tickets ticket = new Tickets(data.title(), data.description(), data.priority(), hours, createdBy);
        ticketsRepository.save(ticket);
        return new TicketsResponse(ticket);
    }

    public PagedModel<TicketsResponse> getAll(Pageable pageable) {
        PagedModel<TicketsResponse> tickets = new PagedModel<>(ticketsRepository.findAll(pageable).map(TicketsResponse::new));
        return tickets;
    }

    public TicketsResponse getTicketById(Long id) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        return new TicketsResponse(ticket);
    }

    @Transactional
    public TicketsResponse updateTicket(Long id, UpdateTicketRequest data) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        ticket.updateTicket(data.title(), data.description());

        return new TicketsResponse(ticket);
    }

    @Transactional
    public TicketsResponse assignTicket(Long id, AssignTicketRequest data) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        User agent = userRepository.findByLogin(data.login()).orElseThrow(
                () -> new EntityNotFoundException("User not found with login: " + data.login())
        );

        ticket.checkStatusAssign();
        ticket.setAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(StatusEnum.EM_PROGRESSO);

        return new TicketsResponse(ticket);
    }

    @Transactional
    public TicketsResponse resolveTicket(Long id) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        ticket.checkStatusResolve();
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(StatusEnum.RESOLVIDO);

        return new TicketsResponse(ticket);
    }

    @Transactional
    public TicketsResponse closeTicket(Long id) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        ticket.checkStatusClose();
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(StatusEnum.FECHADO);

        return new TicketsResponse(ticket);
    }

    @Transactional
    public TicketsResponse returnTicket(Long id, ReturnTicketRequest data) {
        Tickets ticket = ticketsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ticket not found with id: " + id)
        );

        ticket.checkStatusClose();
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setDescription(data.description());
        ticket.setStatus(StatusEnum.EM_PROGRESSO);

        return new TicketsResponse(ticket);
    }
}

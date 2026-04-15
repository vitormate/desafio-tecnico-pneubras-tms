package com.pneubras.tms.service;

import com.pneubras.tms.dto.request.CreateTicketsRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.repository.TicketsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.pneubras.tms.entity.Tickets;

@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;

    public TicketsService(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    public ResponseEntity<TicketsResponse> createTickets(CreateTicketsRequest data, UriComponentsBuilder uriBuilder) {
        int hours = Tickets.checkDueHour(data.priority());
        Tickets ticket = new Tickets(data.title(), data.description(), data.priority(), hours);
        ticketsRepository.save(ticket);
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
        TicketsResponse dto = new TicketsResponse(ticket);
        return ResponseEntity.created(uri).body(dto);
    }
}

package com.pneubras.tms.dto.response;

import com.pneubras.tms.entity.Tickets;
import com.pneubras.tms.entity.User;
import com.pneubras.tms.utils.enums.PriorityEnum;
import com.pneubras.tms.utils.enums.StatusEnum;

import java.time.LocalDateTime;

public record TicketsResponse(
        Long id,
        String title,
        String description,
        PriorityEnum priority,
        StatusEnum status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime dueAt,
        String createdBy,
        String agent
) {
    public TicketsResponse(Tickets t) {
        this(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getPriority(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getDueAt(),
                t.getCreatedBy().getLogin(),
                t.getAgent() != null ? t.getAgent().getLogin() : ""
        );
    }
}

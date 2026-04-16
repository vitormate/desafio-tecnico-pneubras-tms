package com.pneubras.tms.dto.request;

import jakarta.validation.constraints.NotNull;

public record AssignTicketRequest(
        @NotNull String login
) {
}

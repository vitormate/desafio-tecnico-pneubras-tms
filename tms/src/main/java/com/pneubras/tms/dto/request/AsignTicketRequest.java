package com.pneubras.tms.dto.request;

import jakarta.validation.constraints.NotNull;

public record AsignTicketRequest(
        @NotNull String login
) {
}

package com.pneubras.tms.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReturnTicketRequest(
        @NotBlank String description
) {
}

package com.pneubras.tms.dto.request;

import com.pneubras.tms.utils.enums.PriorityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketsRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull PriorityEnum priority,
        @NotBlank String login
) {
}

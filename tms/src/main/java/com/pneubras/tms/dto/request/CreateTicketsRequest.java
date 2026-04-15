package com.pneubras.tms.dto.request;

import com.pneubras.tms.utils.enums.PriorityEnum;
import jakarta.validation.constraints.NotBlank;

public record CreateTicketsRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank PriorityEnum priority
) {
}

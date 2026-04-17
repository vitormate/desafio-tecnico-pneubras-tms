package com.pneubras.tms.dto.request;

import com.pneubras.tms.utils.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(@NotBlank String login, @NotBlank String password, @NotNull RoleEnum role) {
}

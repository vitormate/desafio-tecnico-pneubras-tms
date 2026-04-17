package com.pneubras.tms.dto.response;

import com.pneubras.tms.utils.enums.RoleEnum;

public record RegisterResponse(String login, RoleEnum role) {
}

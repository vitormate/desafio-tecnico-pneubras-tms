package com.pneubras.tms.dto.response;

import java.time.LocalDateTime;

public record CustomErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
}

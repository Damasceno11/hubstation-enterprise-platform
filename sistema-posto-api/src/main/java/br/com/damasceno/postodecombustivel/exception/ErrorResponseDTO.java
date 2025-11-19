package br.com.damasceno.postodecombustivel.exception;

import java.time.Instant;

public record ErrorResponseDTO(
        Instant timestamp, Integer status, String error, String message, String path
) {}

package dev.thiagooliveira.poc_autofill_processor.infrastructure.web.model;

import java.time.LocalDateTime;

public record Error(LocalDateTime timestamp, String message, String details) {
}

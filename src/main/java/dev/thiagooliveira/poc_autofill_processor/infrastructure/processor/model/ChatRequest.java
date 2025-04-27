package dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model;

import java.util.List;

public record ChatRequest(String model, List<Message> messages, int n, double temperature) {
}

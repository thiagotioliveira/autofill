package dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model;

public record Message(String role, String content) {

    public String content() {
        return content.replace("```json\n", "").replace("```", "");
    }
}

package dev.thiagooliveira.poc_autofill_processor.infrastructure.processor;

import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model.ChatRequest;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model.ChatResponse;
import org.springframework.web.client.RestTemplate;

public class OpenAIClient {

    private final String apiUrl;
    private final RestTemplate restTemplate;

    public OpenAIClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + apiKey);
            return execution.execute(request, body);
        });
    }

    public ChatResponse send(ChatRequest request) {
        return this.restTemplate.postForObject(this.apiUrl, request, ChatResponse.class);
    }
}

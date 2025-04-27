package dev.thiagooliveira.poc_autofill_processor.config;

import dev.thiagooliveira.poc_autofill_processor.config.props.FormProperties;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.OpenAIClient;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.OpenAIProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenIAProcessorConfig {

    @Bean
    public OpenAIClient openApiClient(
            @Value("${openai.apiUrl}") String apiUrl,
            @Value("${openai.apiKey}") String apiKey) {
        return new OpenAIClient(apiUrl, apiKey);
    }

    @Bean
    public OpenAIProcessor openAIProcessor(
            @Value("${openai.model}") String model,
            @Value("${openai.apiUrl}") String apiUrl,
            @Value("${openai.apiKey}") String apiKey,
            @Value("${openai.max-completions}") int maxCompletions,
            @Value("${openai.temperature}") int temperature,
            FormProperties formProperties) {
        return new OpenAIProcessor(model, apiUrl, apiKey, maxCompletions, temperature, formProperties);
    }

}

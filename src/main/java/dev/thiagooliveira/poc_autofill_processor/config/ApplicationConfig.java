package dev.thiagooliveira.poc_autofill_processor.config;

import dev.thiagooliveira.poc_autofill_processor.domain.URLGeneratorService;
import dev.thiagooliveira.poc_autofill_processor.domain.processor.Processor;
import dev.thiagooliveira.poc_autofill_processor.domain.user.UserRepository;
import dev.thiagooliveira.poc_autofill_processor.domain.user.UserService;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.Extractor;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.extractor.PDFExtractor;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.OpenAIClient;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.OpenAIProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public Extractor extractor() {
        return new PDFExtractor();
    }

    @Bean
    public URLGeneratorService urlGeneratorService(
            @Value("${openai.formBaseUrl}") String formBaseUrl,
            Extractor extractor,
            Processor processor,
            UserService userService) {
        return new URLGeneratorService(formBaseUrl, extractor, processor, userService);
    }

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

package dev.thiagooliveira.poc_autofill_processor.config;

import dev.thiagooliveira.poc_autofill_processor.domain.URLGeneratorService;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.Extractor;
import dev.thiagooliveira.poc_autofill_processor.domain.processor.Processor;
import dev.thiagooliveira.poc_autofill_processor.domain.user.UserRepository;
import dev.thiagooliveira.poc_autofill_processor.domain.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DomainConfig {

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public URLGeneratorService urlGeneratorService(
            @Value("${app.formBaseUrl}") String formBaseUrl,
            Extractor extractor,
            Processor processor,
            UserService userService) {
        return new URLGeneratorService(formBaseUrl, extractor, processor, userService);
    }

}

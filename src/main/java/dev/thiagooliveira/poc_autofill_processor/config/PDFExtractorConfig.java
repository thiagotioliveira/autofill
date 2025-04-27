package dev.thiagooliveira.poc_autofill_processor.config;

import dev.thiagooliveira.poc_autofill_processor.domain.extractor.Extractor;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.extractor.PDFExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PDFExtractorConfig {



    @Bean
    public Extractor extractor() {
        return new PDFExtractor();
    }


}

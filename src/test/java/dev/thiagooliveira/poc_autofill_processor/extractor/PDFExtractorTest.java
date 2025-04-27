package dev.thiagooliveira.poc_autofill_processor.extractor;

import dev.thiagooliveira.poc_autofill_processor.domain.extractor.Extractor;
import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.model.DocumentForExtraction;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.extractor.PDFExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

//@SpringBootTest
class PDFExtractorTest {

    private Extractor processor;

    @BeforeEach
    void setUp() {
        this.processor = new PDFExtractor();
    }

    @Test
    void extract() throws IOException {
        ClassPathResource resource = new ClassPathResource("invoice.pdf");
        try (InputStream inputStream = resource.getInputStream()) {
            RawDocument document = processor.extract(new DocumentForExtraction(inputStream));
            assertThat(document).isNotNull();
            assertThat(document.value()).isNotBlank();
            System.out.println(document.value());
        }
    }
}
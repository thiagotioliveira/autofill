package dev.thiagooliveira.poc_autofill_processor.config;

import dev.thiagooliveira.poc_autofill_processor.domain.extractor.TextExtractor;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.extractor.PDFTextExtractor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@Configuration
public class PDFExtractorConfig {

    @Bean
    public TextExtractor extractor(ITesseract tess4j) {
        return new PDFTextExtractor(tess4j);
    }

    @Bean
    public ITesseract tess4j(ResourceLoader resourceLoader) throws IOException {
        ITesseract tess4j = new Tesseract();
//        tess4j.setDatapath(resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + "tessdata").getFile().getAbsolutePath());
        tess4j.setDatapath("/app/tessdata");
        return tess4j;
    }
}

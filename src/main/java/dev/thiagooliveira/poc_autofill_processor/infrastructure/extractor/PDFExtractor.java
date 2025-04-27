package dev.thiagooliveira.poc_autofill_processor.infrastructure.extractor;

import dev.thiagooliveira.poc_autofill_processor.domain.exception.GenericErrorException;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.Extractor;
import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.model.DocumentForExtraction;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

public class PDFExtractor implements Extractor {

    @Override
    public RawDocument extract(DocumentForExtraction document) {
        try (PDDocument doc = Loader.loadPDF(IOUtils.toByteArray(document.value()))){
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            return new RawDocument(text);
        } catch (IOException e) {
            throw new GenericErrorException(e);
        }
    }
}

package dev.thiagooliveira.poc_autofill_processor.domain.extractor;

import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.model.DocumentForExtraction;

public interface TextExtractor {

    RawDocument extract(DocumentForExtraction document);
}

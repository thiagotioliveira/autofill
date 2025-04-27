package dev.thiagooliveira.poc_autofill_processor.domain.processor;

import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;

import java.util.Map;

public interface Processor {

    Map<String, String> process(RawDocument document);
}

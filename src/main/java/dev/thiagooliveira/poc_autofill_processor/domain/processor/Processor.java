package dev.thiagooliveira.poc_autofill_processor.domain.processor;

import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;
import dev.thiagooliveira.poc_autofill_processor.domain.user.User;

import java.util.Map;

public interface Processor {

    Map<String, String> process(User user, RawDocument document);
}

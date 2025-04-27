package dev.thiagooliveira.poc_autofill_processor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "form")
public class FormProperties {
    private Map<String, FieldProperties> fields = new HashMap<>();

    public Map<String, FieldProperties> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldProperties> fields) {
        this.fields = fields;
    }
}

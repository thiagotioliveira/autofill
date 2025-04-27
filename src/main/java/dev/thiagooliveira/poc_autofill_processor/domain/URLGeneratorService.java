package dev.thiagooliveira.poc_autofill_processor.domain;

import dev.thiagooliveira.poc_autofill_processor.domain.exception.DomainException;
import dev.thiagooliveira.poc_autofill_processor.domain.exception.GenericErrorException;
import dev.thiagooliveira.poc_autofill_processor.domain.user.User;
import dev.thiagooliveira.poc_autofill_processor.domain.user.UserService;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.Extractor;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.model.DocumentForExtraction;
import dev.thiagooliveira.poc_autofill_processor.domain.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class URLGeneratorService {
    private static Logger logger = LoggerFactory.getLogger(URLGeneratorService.class);

    private final String formBaseUrl;
    private final Extractor extractor;
    private final Processor processor;
    private final UserService userService;

    public URLGeneratorService(String formBaseUrl, Extractor extractor, Processor processor, UserService userService) {
        this.formBaseUrl = formBaseUrl;
        this.extractor = extractor;
        this.processor = processor;
        this.userService = userService;
    }

    public URL generate(User user, InputStream inputStream) {
        if(!"ADMIN".equalsIgnoreCase(user.getRole()) && !user.hasLimit()){
            throw new DomainException("limit reached");
        }
        Map<String, String> params = this.processor.process(user, this.extractor.extract(new DocumentForExtraction(inputStream)));

        this.userService.computeRequest(user);
        String queryString = params.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        try {
            URL url = URI.create(formBaseUrl + "?" + queryString).toURL();
            logger.info("Generated URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            throw new GenericErrorException(e);
        }
    }
}

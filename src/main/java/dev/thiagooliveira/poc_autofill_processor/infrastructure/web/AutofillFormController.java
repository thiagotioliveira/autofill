package dev.thiagooliveira.poc_autofill_processor.infrastructure.web;

import dev.thiagooliveira.poc_autofill_processor.config.security.AuthenticatedUser;
import dev.thiagooliveira.poc_autofill_processor.domain.URLGeneratorService;
import dev.thiagooliveira.poc_autofill_processor.domain.exception.DomainException;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.web.model.AutofillResponse;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.web.model.FormURL;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/autofill-form")
public class AutofillFormController {

    private static final Long MAX_SIZE_IN_BYTES = 5L * 1024 * 1024; // 5MB

    private final URLGeneratorService urlGeneratorService;

    public AutofillFormController(URLGeneratorService urlGeneratorService) {
        this.urlGeneratorService = urlGeneratorService;
    }

    @PostMapping
    public ResponseEntity<AutofillResponse> processPdf(@AuthenticationPrincipal AuthenticatedUser user, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new DomainException("file is empty");
        }
        if(file.getSize() > MAX_SIZE_IN_BYTES) {
            throw new DomainException("file is too big. (limit 5MB)");
        }
        try {
            URL url = this.urlGeneratorService.generate(user.getUser(), file.getInputStream());
            return ResponseEntity.ok(new AutofillResponse(user.getUser(), new FormURL(url.toString())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

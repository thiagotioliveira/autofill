package dev.thiagooliveira.poc_autofill_processor.infrastructure.web.model;

import dev.thiagooliveira.poc_autofill_processor.domain.user.User;

public record AutofillResponse(User user, FormURL formURL) {
}

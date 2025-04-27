package dev.thiagooliveira.poc_autofill_processor.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthenticatedUser extends User {
    private dev.thiagooliveira.poc_autofill_processor.domain.user.User user;

    public AuthenticatedUser(dev.thiagooliveira.poc_autofill_processor.domain.user.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public dev.thiagooliveira.poc_autofill_processor.domain.user.User getUser() {
        return user;
    }
}

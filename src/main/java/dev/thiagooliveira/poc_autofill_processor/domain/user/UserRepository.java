package dev.thiagooliveira.poc_autofill_processor.domain.user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    User save(User user);
}

package dev.thiagooliveira.poc_autofill_processor.domain.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void computeRequest(User user) {
        user.decreaseLimit();
        this.userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}


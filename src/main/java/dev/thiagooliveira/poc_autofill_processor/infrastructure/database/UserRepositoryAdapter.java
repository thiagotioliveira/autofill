package dev.thiagooliveira.poc_autofill_processor.infrastructure.database;

import dev.thiagooliveira.poc_autofill_processor.domain.user.User;
import dev.thiagooliveira.poc_autofill_processor.domain.user.UserRepository;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.database.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final dev.thiagooliveira.poc_autofill_processor.infrastructure.database.UserRepository userRepository;

    public UserRepositoryAdapter(dev.thiagooliveira.poc_autofill_processor.infrastructure.database.UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findById(username)
                .map(u -> new User(u.getName(), u.getUsername(), u.getEmail(), u.getPassword(), u.getRole(), u.getLimit(), u.getCommercialCode(), u.getTeamName(), u.getVersion()));
    }

    @Override
    public User save(User user) {
        UserEntity userSaved = this.userRepository.save(new UserEntity(user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getLimit(), user.getCommercialCode(), user.getTeamName(), user.getVersion()));
        return new User(userSaved.getName(), userSaved.getUsername(), userSaved.getEmail(), userSaved.getPassword(), userSaved.getRole(), userSaved.getLimit(), userSaved.getCommercialCode(), userSaved.getTeamName(), userSaved.getVersion());
    }
}

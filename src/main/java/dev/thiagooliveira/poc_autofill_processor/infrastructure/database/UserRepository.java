package dev.thiagooliveira.poc_autofill_processor.infrastructure.database;

import dev.thiagooliveira.poc_autofill_processor.infrastructure.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}

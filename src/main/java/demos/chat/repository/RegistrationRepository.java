package demos.chat.repository;

import demos.chat.entity.Group;
import demos.chat.entity.Registration;
import demos.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    Optional<Registration> findByUserAndGroup(User user, Group group);
}

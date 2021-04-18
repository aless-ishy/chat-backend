package demos.chat.repository;

import demos.chat.entity.Group;
import demos.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Page<Message> findAllByRegistration_GroupOrderByCreatedAsc(Group group, Pageable pageable);
}

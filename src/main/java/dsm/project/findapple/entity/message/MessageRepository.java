package dsm.project.findapple.entity.message;

import dsm.project.findapple.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findTopByChatOrderBySendAtDesc(Chat chat);
}

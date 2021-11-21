package dsm.project.findapple.entity.images.message;

import dsm.project.findapple.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageImageRepository extends JpaRepository<MessageImage, Long> {
    Optional<MessageImage> findByMessage(Message message);
    void deleteByMessage(Message message);
}

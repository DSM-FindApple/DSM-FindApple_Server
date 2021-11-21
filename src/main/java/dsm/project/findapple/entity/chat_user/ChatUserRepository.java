package dsm.project.findapple.entity.chat_user;

import dsm.project.findapple.entity.chat.Chat;
import dsm.project.findapple.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Page<ChatUser> findAllByUser(User user, Pageable pageable);
    Optional<ChatUser> findByChatAndUserNot(Chat chat, User user);
}

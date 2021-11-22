package dsm.project.findapple.entity.chat_user;

import dsm.project.findapple.entity.chat.Chat;
import dsm.project.findapple.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatUserId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private Boolean isEntered;
}

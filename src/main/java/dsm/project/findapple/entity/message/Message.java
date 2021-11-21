package dsm.project.findapple.entity.message;

import dsm.project.findapple.entity.chat.Chat;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.payload.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    private String message;

    private LocalDateTime sendAt;

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    public Message updateMessage(String message) {
        this.message = message;

        return this;
    }
}

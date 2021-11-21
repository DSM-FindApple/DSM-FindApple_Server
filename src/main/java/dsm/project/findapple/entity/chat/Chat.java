package dsm.project.findapple.entity.chat;

import dsm.project.findapple.entity.chat_user.ChatUser;
import dsm.project.findapple.entity.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    private String chatId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatUser> chatUsers;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;
}

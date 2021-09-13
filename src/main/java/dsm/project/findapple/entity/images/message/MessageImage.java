package dsm.project.findapple.entity.images.message;

import dsm.project.findapple.entity.message.Message;
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
public class MessageImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageImageId;

    private String imageName;

    @OneToOne
    @JoinColumn(name = "message_id")
    private Message message;
}

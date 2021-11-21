package dsm.project.findapple.entity.promise;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.message.Message;
import dsm.project.findapple.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Promise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long promiseId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    private String script;

    private LocalDateTime meetAt;

    private Boolean isAccept;

    @OneToOne
    @JoinColumn(name = "area_code")
    private Area area;

    @OneToOne
    @JoinColumn(name = "message")
    private Message message;

    public Promise updateIsAccept(Boolean isAccept) {
        this.isAccept = isAccept;

        return this;
    }
}

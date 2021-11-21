package dsm.project.findapple.entity.ban_user;

import dsm.project.findapple.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long banId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ban_user_id")
    private User banUser;

    private LocalDateTime banedAt;
}

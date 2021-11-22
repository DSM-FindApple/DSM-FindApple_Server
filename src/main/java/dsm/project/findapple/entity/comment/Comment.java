package dsm.project.findapple.entity.comment;

import dsm.project.findapple.entity.find.Find;
import dsm.project.findapple.entity.lost.Lost;
import dsm.project.findapple.entity.recomment.Recomment;
import dsm.project.findapple.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lost_id")
    private Lost lost;

    @ManyToOne
    @JoinColumn(name = "find_id")
    private Find find;

    private String comment;

    private LocalDateTime writeAt;

    @OneToMany(mappedBy = "comment")
    private List<Recomment> recomments;
}

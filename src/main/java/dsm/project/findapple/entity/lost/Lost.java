package dsm.project.findapple.entity.lost;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.images.lost.LostImage;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.payload.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Lost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lostId;

    private String title;

    @ManyToOne
    @JoinColumn(name = "kakaoId")
    private User user;

    @OneToOne
    @JoinColumn(name = "lost_area")
    private Area area;

    private String detailInfo;

    private LocalDateTime lostAt;

    private LocalDateTime writeAt;

    @Enumerated(value = EnumType.STRING)
    private Category category;
}

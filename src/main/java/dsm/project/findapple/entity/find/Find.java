package dsm.project.findapple.entity.find;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.images.find.FindImage;
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
public class Find {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long findId;

    private String title;

    @ManyToOne
    @JoinColumn(name = "kakaoId")
    private User user;

    @OneToOne
    @JoinColumn(name = "lost_area")
    private Area area;

    private String detailInfo;

    private LocalDateTime findAt;

    private LocalDateTime writeAt;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "find", cascade = CascadeType.ALL)
    private List<FindImage> findImages;

    @OneToMany(mappedBy = "find", cascade = CascadeType.ALL)
    private List<Comment> comments;
}

package dsm.project.findapple.entity.user;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.find.Find;
import dsm.project.findapple.entity.lost.Lost;
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
public class User {
    @Id
    private Long kakaoId;

    private String kakaoNickName;

    private Integer point;

    @OneToMany(mappedBy = "user")
    private List<Lost> losts;

    @OneToMany(mappedBy = "user")
    private List<Find> finds;

    @OneToOne
    @JoinColumn(name = "visit_area")
    private Area area;

    public User updateUserName(String kakaoNickName) {
        this.kakaoNickName = kakaoNickName;

        return this;
    }
}

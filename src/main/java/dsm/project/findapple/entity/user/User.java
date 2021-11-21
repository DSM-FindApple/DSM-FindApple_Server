package dsm.project.findapple.entity.user;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.ban_user.BanUser;
import dsm.project.findapple.entity.chat_user.ChatUser;
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

    private String profileUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Lost> losts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Find> finds;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatUser> chatUsers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BanUser> banUsers;

    @OneToOne
    @JoinColumn(name = "visit_area")
    private Area area;

    public User updateUserName(String kakaoNickName) {
        this.kakaoNickName = kakaoNickName;

        return this;
    }

    public User updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;

        return this;
    }

    public User upPoint(Integer point) {
        this.point += point;

        return this;
    }

    public User downPoint(Integer point) {
        this.point -= point;

        return this;
    }
}

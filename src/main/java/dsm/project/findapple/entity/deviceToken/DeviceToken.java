package dsm.project.findapple.entity.deviceToken;

import dsm.project.findapple.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {
    @Id
    private Long tokenId;

    private String deviceToken;

    @ManyToOne
    @JoinColumn(name = "kakaoId")
    private User user;
}

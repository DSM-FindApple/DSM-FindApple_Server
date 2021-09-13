package dsm.project.findapple.entity.images.lost;

import dsm.project.findapple.entity.lost.Lost;
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
public class LostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lostImageId;

    private String lostImageName;

    @ManyToOne
    @JoinColumn(name = "lost_id")
    private Lost lost;
}

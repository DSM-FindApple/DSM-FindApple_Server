package dsm.project.findapple.payload.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    EAR_PHONE("이어폰"),
    CELL_PHONE("휴대폰"),
    MACHINE("전자기기"),
    CLOTHES("의류"),
    ACC("악세사리"),
    WALLET("지갑"),
    CAR("지동차");

    private String category;
}

package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private Long menuNo;
    private String menuName;
    private String menuImage;
    private String menuPrice;
    private String menuExplain;

    public static MenuDTO fromEntity(Menu menu) {
        return new MenuDTO(
                menu.getMenuNo(),
                menu.getMenuName(),
                menu.getMenuImage(),
                menu.getMenuPrice(),
                menu.getMenuExplain()
        );
    }
}

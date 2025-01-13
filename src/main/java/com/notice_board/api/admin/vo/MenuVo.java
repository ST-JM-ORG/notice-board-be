package com.notice_board.api.admin.vo;

import com.notice_board.model.menu.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;


@Data
@Builder
public class MenuVo {
    @Schema(description = "PK")
    private Long id;

    @Schema(description = "메뉴 이름")
    private String menuNm;

    @Schema(description = "메뉴 코드")
    private String menuCode;

    @Schema(description = "메뉴 설명")
    private String summary;

    @Schema(description = "메뉴 정렬 순서 (카테고리 별)")
    private Long sortOrder;

    @Schema(description = "카테고리 정보")
    private CategoryVo category;

    @Schema(description = "최초 등록일")
    private String createdDate;

    @Schema(description = "마지막 수정일")
    private String modifiedDate;

    public static MenuVo toVO(Menu menu) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return MenuVo.builder()
                .id(menu.getId())
                .menuNm(menu.getMenuNm())
                .menuCode(menu.getMenuCode())
                .summary(menu.getSummary())
                .category(menu.getCategory() != null ? CategoryVo.toVO(menu.getCategory()) : null)
                .sortOrder(menu.getSortOrder())
                .createdDate(formatter.format(menu.getCreatedDate()))
                .modifiedDate(formatter.format(menu.getModifiedDate()))
                .build();
    }
}

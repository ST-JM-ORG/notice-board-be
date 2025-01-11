package com.notice_board.api.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class MenuDto {

    @Schema(description = "메뉴 이름 (필수)")
    private String menuNm;

    @Schema(description = "메뉴 코드 (필수)")
    private String menuCode;

    @Schema(description = "메뉴 설명", nullable = true)
    private String summary;
}

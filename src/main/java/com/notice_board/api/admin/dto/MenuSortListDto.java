package com.notice_board.api.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class MenuSortListDto {

    @Schema(description = "메뉴 ID (필수)")
    private Long id;

    @Schema(description = "순서 (필수)")
    private Long sortOrder;
}

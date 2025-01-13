package com.notice_board.api.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class MenuSearchReqDto {
    @Schema(description = "카테고리 ID")
    private Long categoryId;

}

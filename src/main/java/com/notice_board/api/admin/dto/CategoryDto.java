package com.notice_board.api.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class CategoryDto {

    @Schema(description = "카테고리명 (필수)")
    private String categoryNm;
}

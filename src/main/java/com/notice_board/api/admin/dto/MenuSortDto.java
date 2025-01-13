package com.notice_board.api.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class MenuSortDto {

    @Schema(description = "카테고리 ID (필수)")
    private Long categoryId;

    @Schema(description = "메뉴 정렬 List (필수)")
    private List<MenuSortListDto> menuSortList = new ArrayList<>();
}

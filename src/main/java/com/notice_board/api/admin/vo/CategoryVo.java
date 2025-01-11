package com.notice_board.api.admin.vo;

import com.notice_board.model.menu.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;


@Data
@Builder
public class CategoryVo {
    @Schema(description = "PK")
    private Long id;

    @Schema(description = "카테고리 명")
    private String categoryNm;

    @Schema(description = "카테고리 정렬 순서")
    private Long sortOrder;

    @Schema(description = "최초 등록일")
    private String createdDate;

    @Schema(description = "마지막 수정일")
    private String modifiedDate;

    public static CategoryVo toVO(Category category) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return CategoryVo.builder()
                .id(category.getId())
                .categoryNm(category.getCategoryNm())
                .sortOrder(category.getSortOrder())
                .createdDate(formatter.format(category.getCreatedDate()))
                .modifiedDate(formatter.format(category.getModifiedDate()))
                .build();
    }
}

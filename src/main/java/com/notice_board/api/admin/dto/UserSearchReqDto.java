package com.notice_board.api.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UserSearchReqDto {

    @Schema(description = "검색 타입 (ALL, EMAIL, NAME, CONTACT)", defaultValue = "ALL")
    private SearchType searchType = SearchType.ALL;

    @Schema(description = "검색 키워드")
    private String keyword;

    public enum SearchType {
        @Schema(description = "전체 검색")
        ALL,
        @Schema(description = "이메일로 검색")
        EMAIL,
        @Schema(description = "이름으로 검색")
        NAME,
        @Schema(description = "연락처로 검색")
        CONTACT
    }
}

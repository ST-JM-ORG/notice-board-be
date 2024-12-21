package com.notice_board.api.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class EditPwDto {
    @Schema(description = "기존 비밀번호")
    private String currentPw;

    @Schema(description = "새 비밀번호")
    private String newPw;
}

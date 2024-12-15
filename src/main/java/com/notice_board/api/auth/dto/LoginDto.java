package com.notice_board.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class LoginDto {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String password;
}

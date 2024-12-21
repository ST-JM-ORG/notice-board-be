package com.notice_board.api.auth.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notice_board.model.Auth.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class MemberVo {
    @JsonIgnore
    private Long id;

    @Schema(description = "이메일")
    private String email;

    @JsonIgnore
    private String password;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "연락처")
    private String contact;

    @JsonIgnore
    private Member.UserType userType;
}

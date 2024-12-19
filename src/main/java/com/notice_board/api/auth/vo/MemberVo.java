package com.notice_board.api.auth.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notice_board.model.Auth.Member;
import lombok.Data;


@Data
public class MemberVo {
    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private String contact;

    private Member.UserType userType;
}

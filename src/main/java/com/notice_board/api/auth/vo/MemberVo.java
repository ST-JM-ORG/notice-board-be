package com.notice_board.api.auth.vo;

import com.notice_board.model.Member;
import lombok.Data;


@Data
public class MemberVo {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String contact;

    private Member.UserType userType;
}

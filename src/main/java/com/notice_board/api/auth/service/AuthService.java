package com.notice_board.api.auth.service;

import com.notice_board.api.auth.dto.MemberDto;

import java.io.IOException;

public interface AuthService {
    void checkEmail(String email);

    void signUp(MemberDto memberDto) throws IOException;

    void validPassword(String password);

    void validEmail(String email);
}

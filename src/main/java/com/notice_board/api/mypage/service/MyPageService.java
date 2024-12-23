package com.notice_board.api.mypage.service;

import com.notice_board.api.mypage.dto.EditPwDto;
import com.notice_board.api.mypage.dto.EditMemberDto;

import java.io.IOException;

public interface MyPageService {
    void editUser(Long memberId, EditMemberDto memberDto) throws IOException;

    void editPassword(Long memberId, EditPwDto editPwDto);

    void deleteUser(Long memberId);
}

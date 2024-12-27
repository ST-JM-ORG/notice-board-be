package com.notice_board.api.mypage.service;

import com.notice_board.api.mypage.dto.EditPwDto;
import com.notice_board.api.mypage.dto.EditMemberDto;

public interface MyPageService {
    void editUser(Long memberId, EditMemberDto memberDto);

    void editPassword(Long memberId, EditPwDto editPwDto);

    void deleteUser(Long memberId);
}

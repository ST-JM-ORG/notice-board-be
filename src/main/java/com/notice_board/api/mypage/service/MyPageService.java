package com.notice_board.api.mypage.service;

import com.notice_board.api.auth.dto.MemberDto;
import com.notice_board.api.mypage.dto.EditMemberDto;

import java.io.IOException;

public interface MyPageService {

    void editInfo(Long memberId, EditMemberDto memberDto) throws IOException;
}

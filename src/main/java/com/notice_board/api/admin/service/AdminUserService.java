package com.notice_board.api.admin.service;

import com.notice_board.api.admin.dto.AdminEditMemberDto;
import com.notice_board.api.admin.dto.UserSearchReqDto;
import com.notice_board.api.admin.vo.AdminMemberVo;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.mypage.dto.EditMemberDto;
import com.notice_board.common.component.CustomPageable;
import com.notice_board.common.component.PaginationResDto;

import java.io.IOException;

public interface AdminUserService {
    PaginationResDto<AdminMemberVo> getUserList(CustomPageable customPageable, UserSearchReqDto reqDto, MemberVo loginMember);

    AdminMemberVo getUserDetail(Long id, MemberVo loginMember);

    void editUser(Long memberId, AdminEditMemberDto editMemberDto, MemberVo loginMember);

    void deleteUser(Long memberId, MemberVo loginMember);
}

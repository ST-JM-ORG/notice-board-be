package com.notice_board.api.admin.controller;

import com.notice_board.api.admin.dto.UserSearchReqDto;
import com.notice_board.api.admin.service.AdminUserService;
import com.notice_board.api.admin.vo.AdminMemberVo;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.annotation.AuthMember;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.component.CustomPageable;
import com.notice_board.common.component.PaginationResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
@CrossOrigin("*")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
@Tag(name = "관리자 회원 관리 API", description = "관리자 회원 관리 API 모음.")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @Operation(summary = "회원 목록 가져오기")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<PaginationResDto<AdminMemberVo>> getUserList(@ParameterObject CustomPageable pageable, @ParameterObject UserSearchReqDto reqDto, @AuthMember MemberVo memberVo) {
        return BaseResponse.from(adminUserService.getUserList(pageable, reqDto, memberVo));
    }
}

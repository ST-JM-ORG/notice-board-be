package com.notice_board.api.admin.controller;

import com.notice_board.api.admin.dto.AdminEditMemberDto;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public BaseResponse<PaginationResDto<AdminMemberVo>> getUserList(@ParameterObject CustomPageable pageable
            , @ParameterObject UserSearchReqDto reqDto, @AuthMember MemberVo memberVo) {
        return BaseResponse.from(adminUserService.getUserList(pageable, reqDto, memberVo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 상세 조회")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<AdminMemberVo> getUserDetail(@Schema(description = "회원 PK") @PathVariable Long id, @AuthMember MemberVo memberVo) {
        return BaseResponse.from(adminUserService.getUserDetail(id, memberVo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 정보 수정", description = "데이터는 반드시 `form-data`로 전송. adminYn의 경우 SUPER_ADMIN 만(필수) 사용",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/x-www-form-urlencoded",
                            schema = @Schema(allOf = {AdminEditMemberDto.class},
                                    requiredProperties = {"name", "profileDelYn"}
                            ))
            )
    )
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.FILE_UPLOAD_FAIL
            , CommonExceptionResultMessage.FILE_EXT_FAIL
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.INPUT_VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> editUser(@ModelAttribute AdminEditMemberDto adminMemberDto
            , @Schema(description = "회원 PK") @PathVariable Long id, @AuthMember MemberVo memberVo) {
        adminUserService.editUser(id, adminMemberDto, memberVo);
        return BaseResponse.from(true);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 탈퇴")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> deleteUser(@Schema(description = "회원 PK") @PathVariable Long id, @AuthMember MemberVo memberVo) {
        adminUserService.deleteUser(id, memberVo);
        return BaseResponse.from(true);
    }
}

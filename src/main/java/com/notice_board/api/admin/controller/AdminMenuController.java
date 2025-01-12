package com.notice_board.api.admin.controller;

import com.notice_board.api.admin.dto.MenuDto;
import com.notice_board.api.admin.service.AdminMenuService;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.mypage.dto.EditPwDto;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.annotation.AuthMember;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/menu")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
@CrossOrigin("*")
@Tag(name = "최고 관리자 메뉴 관리 API", description = "최고 관리자 메뉴 관리 API 모음. (SUPER_ADMIN 접근 가능)")
public class AdminMenuController {

    private final AdminMenuService adminMenuService;

    @GetMapping("/code-check")
    @Operation(
            summary = "메뉴 코드 중복 확인",
            description = "메뉴 코드 데이터를 받아 중복된 메뉴 코드인지 확인",
            parameters = @Parameter(name = "menuCode", description = "메뉴 코드")
    )
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.DUPLICATE_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> checkMenuCode(@RequestParam String menuCode) {
        adminMenuService.checkMenuCode(menuCode);
        return BaseResponse.from(true);
    }

    @PostMapping
    @Operation(summary = "메뉴 등록", description = "`menuNm, menuCode, categoryId` 필수 categoryId가 0일 경우 미선택")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.DUPLICATE_FAIL
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> createMenu(@RequestBody MenuDto menuDto) {
        adminMenuService.createMenu(menuDto);
        return BaseResponse.from(true);
    }
}

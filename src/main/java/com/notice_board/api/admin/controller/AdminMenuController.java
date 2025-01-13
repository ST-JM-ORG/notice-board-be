package com.notice_board.api.admin.controller;

import com.notice_board.api.admin.dto.*;
import com.notice_board.api.admin.service.AdminMenuService;
import com.notice_board.api.admin.vo.MenuVo;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/menu")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
@CrossOrigin("*")
@Tag(name = "최고 관리자 메뉴 관리 API", description = "최고 관리자 메뉴 관리 API 모음. (SUPER_ADMIN 접근 가능)")
public class AdminMenuController {

    private final AdminMenuService adminMenuService;

    @GetMapping
    @Operation(summary = "메뉴 목록 조회", description = "categoryId가 0일 경우 미선택 메뉴 조회, null 일 경우 모든 메뉴 조회, sortOrder 순으로 정렬 (ASC)")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<List<MenuVo>> getMenuList(@ParameterObject MenuSearchReqDto reqDto) {
        return BaseResponse.from(adminMenuService.getMenuList(reqDto));
    }

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
    @Operation(summary = "메뉴 등록", description = "`menuNm, menuCode, categoryId` 필수 `categoryId`가 0일 경우 미선택")
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

    @GetMapping("/{id}")
    @Operation(summary = "메뉴 상세 조회")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<MenuVo> getMenuDetail(@PathVariable Long id) {
        return BaseResponse.from(adminMenuService.getMenuDetail(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "메뉴 수정", description = "`menuNm, menuCode, categoryId` 필수 `categoryId`가 0일 경우 미선택")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> modifyMenu(@RequestBody MenuDto menuDto, @Schema(description = "메뉴 PK") @PathVariable Long id) {
        adminMenuService.modifyMenu(menuDto, id);
        return BaseResponse.from(true);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "메뉴 삭제")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> deleteMenu(@Schema(description = "메뉴 PK") @PathVariable Long id) {
        adminMenuService.deleteMenu(id);
        return BaseResponse.from(true);
    }

    @PutMapping("/change-sort")
    @Operation(summary = "메뉴 순서 변경", description = "categoryId, 메뉴 데이터를 List 로 전송")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> changeSortOrder(@RequestBody MenuSortDto menuSortDto) {
        adminMenuService.changeSortOrder(menuSortDto);
        return BaseResponse.from(true);
    }
}

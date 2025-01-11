package com.notice_board.api.admin.controller;

import com.notice_board.api.admin.dto.CategoryDto;
import com.notice_board.api.admin.service.AdminCategoryService;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/category")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
@CrossOrigin("*")
@Tag(name = "최고 관리자 카테고리 관리 API", description = "최고 관리자 카테고리 관리 API 모음. (SUPER_ADMIN 접근 가능)")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @Operation(summary = "카테고리 등록", description = "`categoryNm` 필수")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> createCategory(@RequestBody CategoryDto categoryDto) {
        adminCategoryService.createCategory(categoryDto);
        return BaseResponse.from(true);
    }
}

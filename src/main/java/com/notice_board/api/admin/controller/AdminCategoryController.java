package com.notice_board.api.admin.controller;

import com.notice_board.api.admin.dto.CategoryDto;
import com.notice_board.api.admin.dto.CategorySortDto;
import com.notice_board.api.admin.service.AdminCategoryService;
import com.notice_board.api.admin.vo.CategoryVo;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/{id}")
    @Operation(summary = "카테고리 조회", description = "`categoryNm` 필수")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<CategoryVo> getCategoryDetail(@Schema(description = "카테고리 PK") @PathVariable Long id) {
        return BaseResponse.from(adminCategoryService.getCategoryDetail(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "`categoryNm` 필수")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> modifyCategory(@RequestBody CategoryDto categoryDto, @Schema(description = "카테고리 PK") @PathVariable Long id) {
        adminCategoryService.modifyCategory(categoryDto, id);
        return BaseResponse.from(true);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> deleteCategory(@Schema(description = "카테고리 PK") @PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return BaseResponse.from(true);
    }

    @PutMapping("/change-sort")
    @Operation(summary = "카테고리 순서 변경", description = "카테고리 데이터를 List 로 전송")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.ACCESS_DENIED
            , CommonExceptionResultMessage.NOT_FOUND
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> changeSortOrder(@RequestBody List<CategorySortDto> list) {
        adminCategoryService.changeSortOrder(list);
        return BaseResponse.from(true);
    }
}

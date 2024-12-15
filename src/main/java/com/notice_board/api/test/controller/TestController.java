package com.notice_board.api.test.controller;

import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.component.JSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@CrossOrigin("*")
@Tag(name = "Test API")
public class TestController {

    @GetMapping("/token")
    @Operation(summary = "토큰 테스트")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            ,CommonExceptionResultMessage.ACCESS_DENIED
            ,CommonExceptionResultMessage.JWT_TOKEN_EXPIRED
    })
    public BaseResponse tokenTest(@Parameter(hidden = true) BaseResponse res) {
        res.setJsonResult(JSONResult.successBuilder());
        return res;
    }

    @GetMapping("/admin/token")
    @Operation(summary = "Admin 토큰 테스트")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            ,CommonExceptionResultMessage.ACCESS_DENIED
            ,CommonExceptionResultMessage.JWT_TOKEN_EXPIRED
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BaseResponse adminTokenTest(@Parameter(hidden = true) BaseResponse res) {
        res.setJsonResult(JSONResult.successBuilder());
        return res;
    }
}

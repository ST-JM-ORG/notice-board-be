package com.notice_board.api.auth.controller;

import com.notice_board.api.auth.service.AuthService;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.JSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "Auth 관련 API 모음.")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/email-check")
    @Operation(
            summary = "이메일 중복 확인",
            description = "이메일 데이터를 받아 중복된 이메일인지 확인",
            parameters = @Parameter(name = "email", description = "이메일")
    )
    public BaseResponse checkEmail(@RequestParam String email, @Parameter(hidden = true) BaseResponse res) {
        authService.checkEmail(email);
        res.setJsonResult(JSONResult.successBuilder());
        return res;
    }
}

package com.notice_board.api.auth.controller;

import com.notice_board.api.auth.dto.MemberDto;
import com.notice_board.api.auth.service.AuthService;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.JSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("/sign-up")
    @Operation(
            summary = "회원 가입",
            description = "회원 가입 요청. 데이터는 반드시 `form-data`로 전송.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(
                            allOf = {MemberDto.class},
                            requiredProperties = {"email", "name", "password"}
                    ))
            )
    )
    public BaseResponse signUp(@ModelAttribute MemberDto memberDto, @Parameter(hidden = true) BaseResponse res) throws IOException {
        authService.signUp(memberDto);
        res.setJsonResult(JSONResult.successBuilder());
        return res;
    }
}

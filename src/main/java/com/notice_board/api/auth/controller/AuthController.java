package com.notice_board.api.auth.controller;

import com.notice_board.api.auth.dto.LoginDto;
import com.notice_board.api.auth.dto.MemberDto;
import com.notice_board.api.auth.service.AuthService;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.auth.vo.TokenVo;
import com.notice_board.common.annotation.ApiErrorCodeExample;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.annotation.AuthMember;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Auth API", description = "Auth 관련 API 모음.")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/email-check")
    @Operation(
            summary = "이메일 중복 확인",
            description = "이메일 데이터를 받아 중복된 이메일인지 확인",
            parameters = @Parameter(name = "email", description = "이메일")
    )
    @ApiErrorCodeExample(CommonExceptionResultMessage.DUPLICATE_FAIL)
    public BaseResponse<Boolean> checkEmail(@RequestParam String email) {
        authService.checkEmail(email);
        return BaseResponse.from(true);

    }

    @PostMapping("/sign-up")
    @Operation(
            summary = "회원 가입",
            description = "회원 가입 요청. 데이터는 반드시 `form-data`로 전송.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/x-www-form-urlencoded",
                            schema = @Schema(allOf = {MemberDto.class},
                                    requiredProperties = {"email", "name", "password"}
                            ))
            )
    )
    @ApiErrorCodeExamples({CommonExceptionResultMessage.DUPLICATE_FAIL
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.FILE_UPLOAD_FAIL
            , CommonExceptionResultMessage.FILE_EXT_FAIL
    })
    public BaseResponse<Boolean> signUp(@ModelAttribute MemberDto memberDto) {
        authService.signUp(memberDto);
        return BaseResponse.from(true);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.LOGIN_FAILED
    })
    public BaseResponse<TokenVo> login(@RequestBody LoginDto loginDto) {
        return BaseResponse.from(authService.login(loginDto));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그인 한 상태로 Header 에 Refresh 전송")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<Boolean> logout(
            @Parameter(name = "Refresh", description = "사용자의 Refresh Token")
            @RequestHeader("Refresh") String refreshToken, @AuthMember MemberVo member) {
        authService.logout(member, refreshToken);
        return BaseResponse.from(true);
    }

    @PostMapping("/reissue-token")
    @Operation(summary = "토큰 재발행", description = "Header 에 Refresh 전송")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.LOGIN_FAILED
            , CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<TokenVo> reissueToken(
            @Parameter(name = "Refresh", description = "사용자의 Refresh Token")
            @RequestHeader("Refresh") String refreshToken) {
        return BaseResponse.from(authService.reissueToken(refreshToken));
    }
}

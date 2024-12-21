package com.notice_board.api.auth.vo;

import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.annotation.AuthMember;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
@CrossOrigin("*")
@Tag(name = "My Page API", description = "마이페이지 API 모음.")
public class MyPageController {

    @GetMapping("/info")
    @Operation(summary = "내 정보 가져오기")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<MemberVo> getMyInfo(@AuthMember MemberVo memberVo) {
        return BaseResponse.from(memberVo);

    }
}

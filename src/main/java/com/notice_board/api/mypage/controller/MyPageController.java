package com.notice_board.api.mypage.controller;

import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.mypage.dto.EditMemberDto;
import com.notice_board.api.mypage.service.MyPageService;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.annotation.AuthMember;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
@CrossOrigin("*")
@Tag(name = "My Page API", description = "마이페이지 API 모음.")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/info")
    @Operation(summary = "내 정보 가져오기")
    @ApiErrorCodeExamples({
            CommonExceptionResultMessage.AUTHENTICATION_FAILED
            , CommonExceptionResultMessage.FAIL
    })
    public BaseResponse<MemberVo> getMyInfo(@AuthMember MemberVo memberVo) {
        return BaseResponse.from(memberVo);
    }

    @PutMapping("/edit")
    @Operation(summary = "내 정보 수정", description = "정보 수정 요청. 데이터는 반드시 `form-data`로 전송.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/x-www-form-urlencoded",
                            schema = @Schema(allOf = {EditMemberDto.class},
                                    requiredProperties = {"name", "profileDelYn"}
                            ))
            )
    )
    @ApiErrorCodeExamples({CommonExceptionResultMessage.VALID_FAIL
            , CommonExceptionResultMessage.DB_FAIL
            , CommonExceptionResultMessage.IMG_UPLOAD_FAIL
            , CommonExceptionResultMessage.NOT_FOUND
    })
    public BaseResponse<Boolean> editInfo(@ModelAttribute EditMemberDto memberDto, @AuthMember MemberVo memberVo) throws IOException {
        myPageService.editInfo(memberVo.getId(), memberDto);
        return BaseResponse.from(true);
    }
}
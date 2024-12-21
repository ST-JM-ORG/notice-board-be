package com.notice_board.api.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class EditMemberDto {
    @Schema(description = "이름")
    private String name;

    @Schema(description = "연락처")
    private String contact;

    @Schema(description = "프로필 이미지")
    private MultipartFile profileImg;

    @Schema(description = "프로필 이미지 삭제 Flag Y, N")
    private String profileDelYn;
}

package com.notice_board.api.auth.dto;

import com.notice_board.model.auth.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class MemberDto {
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "연락처")
    private String contact;

    @Schema(hidden = true)
    private Member.UserType userType;

    @Schema(description = "프로필 이미지")
    private MultipartFile profileImg;
}

package com.notice_board.api.auth.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notice_board.model.auth.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;


@Data
@Builder
public class MemberVo {

    @JsonIgnore
    private Long id;

    @Schema(description = "이메일")
    private String email;

    @JsonIgnore
    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "연락처")
    private String contact;

    @Schema(description = "프로필 이미지 url")
    private String profileImg;

    @JsonIgnore
    private Member.UserType userType;

    public static MemberVo toVO(Member member) {
        String profileImg = Optional.ofNullable(member.getMemberFiles())
                .map(files -> files.get(Member.FileType.PROFILE_IMG))
                .map(profile -> "/file/image/" + Member.FileType.PROFILE_IMG.name() + "/" + profile.getUuid())
                .orElse(StringUtils.EMPTY);

        return MemberVo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .contact(member.getContact())
                .profileImg(profileImg)
                .userType(member.getUserType())
                .build();
    }
}

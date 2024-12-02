package com.notice_board.api.auth.dto;

import com.notice_board.model.Member;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class MemberDto {
    private Long id; // PK
    private String email; // email
    private String password; // pw
    private String name; // 이름
    private String contact; // 연락처
    private Member.UserType userType; // 유저타입
    private MultipartFile profileImg; // 프로필 이미지
}

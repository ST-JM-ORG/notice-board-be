package com.notice_board.api.mypage.service.impl;

import com.notice_board.api.auth.service.AuthService;
import com.notice_board.api.file.dto.FileDto;
import com.notice_board.api.file.service.FileService;
import com.notice_board.api.mypage.dto.EditMemberDto;
import com.notice_board.api.mypage.dto.EditPwDto;
import com.notice_board.api.mypage.service.MyPageService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.common.exception.ValidException;
import com.notice_board.model.auth.Member;
import com.notice_board.model.auth.MemberHis;
import com.notice_board.model.commons.File;
import com.notice_board.repository.MemberHisRepository;
import com.notice_board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service("myPageService")
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements MyPageService {

    private final MemberRepository memberRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    private final MemberHisRepository memberHisRepository;

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void editUser(Long memberId, EditMemberDto editMemberDto) {
        String name = editMemberDto.getName();
        if (StringUtils.isBlank(name)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "name", "이름을 입력해주세요.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        member.setName(name);
        member.setContact(editMemberDto.getContact());
        member.setLastModifyDt(LocalDateTime.now());

        if (StringUtils.equals(editMemberDto.getProfileDelYn(), "Y")) {
            member.getMemberFiles().remove(Member.FileType.PROFILE_IMG);
        }

        MultipartFile profileImg = editMemberDto.getProfileImg();
        if (profileImg != null && !profileImg.isEmpty()) {
            fileService.ExtCheck(new MultipartFile[]{profileImg}, "image"); // 확장자 검사
            FileDto fileDto = fileService.saveFile(profileImg, Member.FileType.PROFILE_IMG.name());
            member.getMemberFiles().put(Member.FileType.PROFILE_IMG, modelMapper.map(fileDto, File.class));
        }

        MemberHis mh = new MemberHis(member);
        memberHisRepository.save(mh);
    }

    @Override
    public void editPassword(Long memberId, EditPwDto editPwDto) {
        String newPw = editPwDto.getNewPw();
        String currentPw = editPwDto.getCurrentPw();

        if (StringUtils.isBlank(newPw)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "newPw", "새 비밀번호를 입력해주세요.");
        }

        if (StringUtils.isBlank(currentPw)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "currentPw", "현재 비밀번호를 입력해주세요.");
        }

        authService.validPassword(newPw);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPw, member.getPassword())) {
            throw new ValidException(CommonExceptionResultMessage.PW_MISMATCH, "currentPw");
        }

        if (StringUtils.equals(newPw, currentPw)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "newPw", "현재 사용 중인 비밀번호입니다.");
        }

        member.setPassword(passwordEncoder.encode(newPw));
        memberRepository.save(member);
    }

    @Override
    public void deleteUser(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        memberRepository.delete(member);
    }
}
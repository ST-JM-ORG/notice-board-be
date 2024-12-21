package com.notice_board.api.mypage.service.impl;

import com.notice_board.api.file.dto.FileDto;
import com.notice_board.api.file.service.FileService;
import com.notice_board.api.mypage.dto.EditMemberDto;
import com.notice_board.api.mypage.service.MyPageService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.model.auth.Member;
import com.notice_board.model.auth.MemberHis;
import com.notice_board.model.commons.File;
import com.notice_board.repository.MemberHisRepository;
import com.notice_board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service("myPageService")
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements MyPageService {

    private final MemberRepository memberRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    private final MemberHisRepository memberHisRepository;

    @Override
    public void editInfo(Long memberId, EditMemberDto editMemberDto) throws IOException {
        String name = editMemberDto.getName();
        if (StringUtils.isEmpty(name)) {
            throw new CustomException(CommonExceptionResultMessage.VALID_FAIL);
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
            if (!fileService.ExtCheck(new MultipartFile[]{profileImg}, "image")) { // 확장자 검사
                throw new CustomException(CommonExceptionResultMessage.IMG_UPLOAD_FAIL, "허용되지 않은 첨부파일 확장자");
            }
            FileDto fileDto = fileService.saveFile(profileImg, Member.FileType.PROFILE_IMG.name());
            member.getMemberFiles().put(Member.FileType.PROFILE_IMG, modelMapper.map(fileDto, File.class));
        }

        MemberHis mh = new MemberHis(member);
        memberHisRepository.save(mh);
    }
}
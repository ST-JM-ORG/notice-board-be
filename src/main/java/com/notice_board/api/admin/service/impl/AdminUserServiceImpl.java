package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.AdminEditMemberDto;
import com.notice_board.api.admin.dto.UserSearchReqDto;
import com.notice_board.api.admin.service.AdminUserService;
import com.notice_board.api.admin.vo.AdminMemberVo;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.file.dto.FileDto;
import com.notice_board.api.file.service.FileService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.component.CustomPageable;
import com.notice_board.common.component.PaginationResDto;
import com.notice_board.common.exception.CustomException;
import com.notice_board.common.exception.ValidException;
import com.notice_board.model.auth.Member;
import com.notice_board.model.auth.MemberHis;
import com.notice_board.model.commons.File;
import com.notice_board.repository.MemberHisRepository;
import com.notice_board.repository.MemberRepository;
import com.notice_board.repository.specification.AdminUserSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service("adminUserService")
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final MemberRepository memberRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    private final MemberHisRepository memberHisRepository;

    @Override
    public PaginationResDto<AdminMemberVo> getUserList(CustomPageable customPageable, UserSearchReqDto reqDto, MemberVo loginMember) {
        Pageable pageable = customPageable.getPageable();

        Specification<Member> spec = AdminUserSpecification.buildSearchSpecification(reqDto, loginMember.getUserType());
        Page<Member> result = memberRepository.findAll(spec, pageable);

        List<AdminMemberVo> adminMemberVoList = result.stream()
                .map(AdminMemberVo::toVO)
                .collect(Collectors.toList());

        return PaginationResDto.<AdminMemberVo>builder()
                .data(adminMemberVoList)
                .total(result.getTotalElements())
                .size(customPageable.getSize())
                .page(customPageable.getPage())
                .build();
    }

    @Override
    public AdminMemberVo getUserDetail(Long id, MemberVo loginMember) {
        Member member = this.getMember(id, loginMember.getUserType());
        return AdminMemberVo.toVO(member);
    }

    @Override
    public void editUser(Long id, AdminEditMemberDto editMemberDto, MemberVo loginMember) {
        String name = editMemberDto.getName();

        // 필수값 체크
        if (StringUtils.isBlank(name)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "name", "이름을 입력해주세요.");
        }

        String profileDelYn = editMemberDto.getProfileDelYn();
        if (!StringUtils.equalsAny(profileDelYn, "Y", "N")) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "profileDelYn", "profileDelYn 값은 'Y' 또는 'N'이어야 합니다.");
        }

        // SUPER_ADMIN 의 경우 adminYn은 필수
        Member.UserType userType = loginMember.getUserType();
        String adminYn = editMemberDto.getAdminYn();
        if (userType == Member.UserType.SUPER_ADMIN && !StringUtils.equalsAny(adminYn, "Y", "N")) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "adminYn", "adminYn 값은 'Y' 또는 'N'이어야 합니다.");
        }

        Member member = this.getMember(id, userType);
        member.setName(name);
        member.setContact(editMemberDto.getContact());
        member.setLastModifyDt(LocalDateTime.now());

        if (userType == Member.UserType.SUPER_ADMIN) {
            member.setUserType(StringUtils.equals(adminYn, "Y") ? Member.UserType.ADMIN : Member.UserType.USER);
        }

        if (StringUtils.equals(profileDelYn, "Y")) {
            member.getMemberFiles().remove(Member.FileType.PROFILE_IMG);
        }

        MemberHis mh = new MemberHis(member);
        memberHisRepository.save(mh);
    }

    @Override
    public void deleteUser(Long id, MemberVo loginMember) {
        Member member = this.getMember(id, loginMember.getUserType());
        memberRepository.delete(member);
    }

    private Member getMember(Long id, Member.UserType userType) {
        if (id == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "회원 PK를 입력해주세요.");
        }

        Member member;
        if (userType == Member.UserType.ADMIN) {
            member = memberRepository.findByIdAndUserType(id, Member.UserType.USER) // ADMIN 일 경우 USER 만 조회
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 조회 실패: ID " + id + "에 해당하는 사용자 없음"));
        } else {
            member = memberRepository.findByIdAndUserTypeNot(id, Member.UserType.SUPER_ADMIN) // SUPER_ADMIN 일 경우 SUPER_ADMIN 빼고 모두 조회
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 조회 실패: ID " + id + "에 해당하는 사용자 없음"));
        }
        return member;
    }
}

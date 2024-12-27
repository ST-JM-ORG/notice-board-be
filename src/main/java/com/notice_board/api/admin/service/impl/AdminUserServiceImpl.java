package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.UserSearchReqDto;
import com.notice_board.api.admin.service.AdminUserService;
import com.notice_board.api.admin.vo.AdminMemberVo;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.component.CustomPageable;
import com.notice_board.common.component.PaginationResDto;
import com.notice_board.common.exception.CustomException;
import com.notice_board.model.auth.Member;
import com.notice_board.repository.MemberRepository;
import com.notice_board.repository.specification.AdminUserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service("adminUserService")
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final MemberRepository memberRepository;

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
        Member member;
        if (loginMember.getUserType() == Member.UserType.ADMIN) {
            member = memberRepository.findByIdAndUserType(id, Member.UserType.USER) // ADMIN 일 경우 USER 만 조회
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 조회 실패: ID " + id + "에 해당하는 사용자 없음"));
        } else {
            member = memberRepository.findByIdAndUserTypeNot(id, Member.UserType.SUPER_ADMIN) // SUPER_ADMIN 일 경우 SUPER_ADMIN 빼고 모두 조회
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "회원 조회 실패: ID " + id + "에 해당하는 사용자 없음"));
        }
        return AdminMemberVo.toVO(member);
    }
}

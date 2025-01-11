package com.notice_board.common.jpa;

import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.common.security.CustomUserDetails;
import com.notice_board.common.security.CustomUserDetailsService;
import com.notice_board.model.auth.Member;
import com.notice_board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Member> {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> getCurrentAuditor() {
        // 현재 인증된 사용자를 반환
        // 예: SecurityContextHolder에서 현재 사용자 조회
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> {
                    // CustomUserDetails에서 MemberVo를 추출하고, 이를 Member로 변환
                    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                    MemberVo memberVo = customUserDetails.getMember();

                    Member member = memberRepository.findById(memberVo.getId())
                            .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED));
                    return member;
                });
    }
}
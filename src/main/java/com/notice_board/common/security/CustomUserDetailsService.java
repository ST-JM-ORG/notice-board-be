package com.notice_board.common.security;

import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.model.Member;
import com.notice_board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String id) {
        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED));

        MemberVo memberVo = modelMapper.map(member, MemberVo.class);
        return new CustomUserDetails(memberVo);
    }
}

package com.notice_board.api.auth.service.impl;

import com.notice_board.api.auth.dto.LoginDto;
import com.notice_board.api.auth.dto.MemberDto;
import com.notice_board.api.auth.service.AuthService;
import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.auth.vo.TokenVo;
import com.notice_board.api.file.dto.FileDto;
import com.notice_board.api.file.service.FileService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.ValidException;
import com.notice_board.common.utils.JwtUtil;
import com.notice_board.common.exception.CustomException;
import com.notice_board.model.auth.BlackList;
import com.notice_board.model.auth.Member;
import com.notice_board.model.auth.MemberHis;
import com.notice_board.model.auth.User;
import com.notice_board.model.commons.File;
import com.notice_board.repository.BlackListRepository;
import com.notice_board.repository.MemberHisRepository;
import com.notice_board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Optional;

@Service("authService")
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    private final JwtUtil jwtUtil;

    private final BlackListRepository blackListRepository;

    private final MemberHisRepository memberHisRepository;

    @Override
    public void checkEmail(String email) {
        this.validEmail(email);

        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new CustomException(CommonExceptionResultMessage.EMAIL_DUPLICATE_FAIL);
        }
    }

    @Override
    public void signUp(MemberDto memberDto) {
        String email = memberDto.getEmail();
        String name = memberDto.getName();
        String password = memberDto.getPassword();

        if (StringUtils.isBlank(name)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "name", "이름을 입력해주세요.");
        }

        this.checkEmail(email);
        this.validPassword(password);

        User user = modelMapper.map(memberDto, User.class);
        user.setPassword(passwordEncoder.encode(password));

        User saveUser = memberRepository.save(user);

        if (saveUser.getId() == null) {
            throw new CustomException(CommonExceptionResultMessage.DB_FAIL, "회원가입에 실패했습니다.");
        }

        MultipartFile profileImg = memberDto.getProfileImg();
        if (profileImg != null && !profileImg.isEmpty()) {
            fileService.ExtCheck(new MultipartFile[]{profileImg}, "image"); // 확장자 검사
            FileDto fileDto = fileService.saveFile(profileImg, Member.FileType.PROFILE_IMG.name());
            saveUser.getMemberFiles().put(Member.FileType.PROFILE_IMG, modelMapper.map(fileDto, File.class));
        }

        MemberHis mh = new MemberHis(saveUser);
        memberHisRepository.save(mh);
    }

    @Override
    public void validEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "email", "이메일을 입력해주세요.");
        }

        // 이메일 형식에 대한 정규식
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regex)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "email", "이메일 형식을 맞춰주세요.");
        }
    }

    @Override
    public TokenVo login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        if (StringUtils.isBlank(email)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "email", "이메일을 입력해주세요.");
        }

        if (StringUtils.isBlank(password)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "password", "비밀번호를 입력해주세요.");
        }

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(CommonExceptionResultMessage.LOGIN_FAILED));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(CommonExceptionResultMessage.LOGIN_FAILED);
        }

        MemberVo memberVo = MemberVo.toVO(member);
        return jwtUtil.generateTokenVo(memberVo);
    }

    @Override
    public void logout(MemberVo loginMember, String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "Refresh", "리프레시 토큰을 입력해주세요.");
        }

        if (!jwtUtil.validateToken(refreshToken)) { // refreshToken 검증
            throw new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED, "유효하지 않는 Refresh Token");
        }

        Long memberId = jwtUtil.getMemberId(refreshToken);
        if (loginMember.getId() != memberId) { // refreshToken 사용자와 비교
            throw new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED, "로그인한 사용자의 Refresh Token 이 아닙니다.");
        }

        Optional<BlackList> blackList = blackListRepository.findByInvalidRefreshToken(refreshToken);
        if (blackList.isPresent()) { // refreshToken BlackList 존재 여부
            throw new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED, "이미 로그아웃 된 사용자입니다.");
        }

        blackListRepository.save(new BlackList(refreshToken));
    }

    @Override
    public TokenVo reissueToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "Refresh", "리프레시 토큰을 입력해주세요.");
        }

        if (!jwtUtil.validateToken(refreshToken)) {  // refreshToken 검증
            throw new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED, "유효하지 않는 Refresh Token");
        }

        Optional<BlackList> blackList = blackListRepository.findByInvalidRefreshToken(refreshToken);
        if (blackList.isPresent()) { // refreshToken BlackList 존재 여부
            throw new CustomException(CommonExceptionResultMessage.AUTHENTICATION_FAILED, "사용할 수 없는 Refresh Token 입니다.");
        }

        Long memberId = jwtUtil.getMemberId(refreshToken);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(CommonExceptionResultMessage.LOGIN_FAILED));

        // 기존 Refresh 토큰 삭제
        blackListRepository.save(new BlackList(refreshToken));

        // 새로운 Token 발급
        MemberVo memberVo = MemberVo.toVO(member);
        return jwtUtil.generateTokenVo(memberVo);
    }

    @Override
    public void validPassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "password", "비밀번호를 입력해주세요.");
        }

        // 영어 대소문자, 숫자, 특수문자 포함, 8~20자
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$";
        if (!password.matches(regex)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "password", "비밀번호 형식을 맞춰주세요.");
        }
    }
}

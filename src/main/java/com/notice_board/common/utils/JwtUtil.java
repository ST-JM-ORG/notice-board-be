package com.notice_board.common.utils;

import com.notice_board.api.auth.vo.MemberVo;
import com.notice_board.api.auth.vo.TokenVo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;


/**
 * <pre>
 * 	JwtUtil
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-12-16 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
public class JwtUtil {

    private final Key key;

    private final long ACCESS_TOKEN_EXP_TIME;

    private final long REFRESH_TOKEN_EXP_TIME;

    public JwtUtil(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expireTime}") long accessTokenExpTime
            , @Value("${jwt.refreshExpireTime}") long refreshTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXP_TIME = accessTokenExpTime;
        this.REFRESH_TOKEN_EXP_TIME = refreshTokenExpTime;
    }


    private String createAccessToken(MemberVo member, ZonedDateTime now) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getId());
        claims.put("name", member.getName());
        claims.put("profileImg", member.getProfileImg());
        claims.put("role", member.getUserType());

        ZonedDateTime tokenValidity = now.plusSeconds(ACCESS_TOKEN_EXP_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.from(tokenValidity)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenVo generateTokenVo(MemberVo memberVo) {
        // 현재 시간
        ZonedDateTime now = ZonedDateTime.now();

        // Access Token 생성
        String accessToken = this.createAccessToken(memberVo, now);

        // Refresh Token 생성
        Claims claims = Jwts.claims();
        claims.put("memberId", memberVo.getId());
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(Instant.from(now.plusSeconds(REFRESH_TOKEN_EXP_TIME))))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenVo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Long getMemberId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

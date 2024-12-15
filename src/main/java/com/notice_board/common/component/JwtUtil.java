package com.notice_board.common.component;

import com.notice_board.api.auth.vo.MemberVo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
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

    private final long accessTokenExpTime;

    public JwtUtil(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expireTime}") long accessTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(MemberVo member) {
        return createToken(member, accessTokenExpTime);
    }


    private String createToken(MemberVo member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getId());
        claims.put("email", member.getEmail());
        claims.put("role", member.getUserType());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        System.out.println(now);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.from(tokenValidity)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Long getUserId(String token) {
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

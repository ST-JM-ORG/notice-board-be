package com.notice_board.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * <pre>
 * Member
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */

@Entity @Setter @Getter
@SQLDelete(sql = "update member set del_dt = now() where id = ?")
@SQLRestriction("del_dt is null")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @Setter(AccessLevel.PROTECTED)
    @Column(nullable = false)
    private LocalDateTime joinDt = LocalDateTime.now();                               // 가입일자

    @Setter(AccessLevel.PROTECTED)
    private LocalDateTime delDt;                                // 탈퇴일자
}

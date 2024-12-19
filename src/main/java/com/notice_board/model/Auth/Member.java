package com.notice_board.model.Auth;

import com.notice_board.model.commons.File;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
@DiscriminatorFormula("case when user_type = 'USER' then 'U' else 'A' end")
abstract public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Long id; // PK

    @Column(nullable = false)
    private String email; // email

    @Column(nullable = false)
    private String password; // pw

    @Column(nullable = false)
    private String name; // 이름

    @Column
    private String contact; // 연락처

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.USER; // 유저타입

    @Setter(AccessLevel.PROTECTED)
    @Column(nullable = false)
    private LocalDateTime joinDt = LocalDateTime.now(); // 가입일자

    @Setter(AccessLevel.PROTECTED)
    private LocalDateTime delDt; // 탈퇴일자

    @ElementCollection
    @CollectionTable(
            name = "member_attach_file",
            joinColumns = @JoinColumn(name = "member_id", nullable = false),
            indexes = {
                    @Index(name = "idx_member_attach_file_01", columnList = "member_id")
            }
    )
    @MapKeyEnumerated(EnumType.ORDINAL)
    private final Map<FileType, File> memberFiles = new HashMap<>();

    public enum FileType {
        PROFILE_IMG, // 프로필 사진
        ;
    }

    public enum UserType {
        USER, ADMIN
    }
}

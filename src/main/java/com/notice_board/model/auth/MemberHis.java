package com.notice_board.model.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_member_his_01", columnList = "member_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MemberHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 이름

    @Column
    private String contact; // 연락처

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter(AccessLevel.PROTECTED)
    @Column(nullable = false)
    private LocalDateTime hisDt = LocalDateTime.now();

    public MemberHis (Member member) {
        this.name = member.getName();
        this.contact = member.getContact();
        this.member = member;
    }
}

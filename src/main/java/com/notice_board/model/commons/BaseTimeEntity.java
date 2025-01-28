package com.notice_board.model.commons;

import com.notice_board.model.auth.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 해당 추상 클래스를 상속할 경우 createDate, modifiedDate 를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)
abstract public class BaseTimeEntity {

    // 생성 시 자동 저장
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    // 수정 시 자동 저장
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    // 생성자 ID
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false, nullable = false)
    private Member createdBy;

    // 수정자 ID
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private Member modifiedBy;
}

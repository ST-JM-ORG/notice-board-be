package com.notice_board.model.commons;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 해당 추상 클래스를 상속할 경우 createDate, modifiedDate 를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)
abstract public class BaseTimeEntity {

    // Entity 가 생성되어 저장될 때 시간이 자동 저장됩니다.
    @CreatedDate
    private LocalDateTime createdDate;

    // 조회한 Entity 값을 변경할 때 시간이 자동 저장됩니다.
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}

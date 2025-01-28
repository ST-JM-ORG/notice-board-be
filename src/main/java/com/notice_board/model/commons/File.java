package com.notice_board.model.commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * <pre>
 * File
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-12-02 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter @EqualsAndHashCode
@Setter @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class File {
    @NonNull
    @Column(nullable = false)
    private String fileName;

    @NonNull
    @Column(nullable = false)
    private Long fileSize;

    @NonNull
    @Column(nullable = false)
    private String ext;

    @NonNull
    @Column(nullable = false)
    private String filePath;

    @NonNull
    @Column(nullable = false)
    private String uuid;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();
}

package com.notice_board.model.post;

import com.notice_board.model.commons.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity @Setter @Getter
@Table(indexes = {
        @Index(name = "idx_post_attachment_01", columnList = "post_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update post_attachment set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class PostAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private File fileInfo;

    @Column(nullable = false)
    private Boolean deleted = false;
}
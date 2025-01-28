package com.notice_board.model.post;

import com.notice_board.model.commons.BaseTimeEntity;
import com.notice_board.model.menu.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;


@Entity @Setter @Getter
@Table(indexes = {
        @Index(name = "idx_post_01", columnList = "menu_id"),
        @Index(name = "idx_post_02", columnList = "writer_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update post set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Column(nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "post")
    @OrderBy("id ASC")
    private final List<PostAttachment> attachmentList = new ArrayList<>();
}
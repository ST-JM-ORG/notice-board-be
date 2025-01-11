package com.notice_board.model.menu;

import com.notice_board.model.commons.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;



@Entity @Setter @Getter
@Table(indexes = {
        @Index(name = "idx_menu_01", columnList = "category_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update menu set menu_code = null, sort_order = null, deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuNm; // 메뉴 이름

    @Column(unique = true)
    private String menuCode; // 메뉴 코드

    @Column
    private String summary; // 메뉴 설명

    @Column
    private Long sortOrder; // 순서 (삭제 시 null)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Boolean deleted = false;
}
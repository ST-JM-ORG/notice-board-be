package com.notice_board.model.menu;

import com.notice_board.model.commons.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;


@Entity @Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update category set deleted = true, sort_order = null where id = ?")
@SQLRestriction("deleted = false")
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryNm; // 카테고리 명

    @Column
    private Long sortOrder; // 순서 (삭제 시 null)

    @Column(nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "category")
    private final List<Menu> menuList = new ArrayList<>();
}
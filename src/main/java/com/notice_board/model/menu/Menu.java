package com.notice_board.model.menu;

import com.notice_board.model.commons.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity @Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update menu set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuNm; // 메뉴 이름

    @Column(nullable = false)
    private String menuCode; // 메뉴 코드

    @Column
    private Long orderBy; // 순서 (삭제 시 null)

    @Column(nullable = false)
    private Boolean deleted = false;
}
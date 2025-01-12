package com.notice_board.repository;

import com.notice_board.model.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuCode(String menuCode);

    Optional<Menu> findTopByCategoryIsNullOrderBySortOrderDesc();
}

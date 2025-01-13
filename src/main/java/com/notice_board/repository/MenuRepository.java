package com.notice_board.repository;

import com.notice_board.model.menu.Category;
import com.notice_board.model.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuCode(String menuCode);

    Optional<Menu> findTopByCategoryIsNullOrderBySortOrderDesc();

    @Modifying
    @Query("UPDATE Menu m SET m.sortOrder = m.sortOrder - 1 " +
            "WHERE m.sortOrder > :sortOrder " +
            "AND (m.category = :category OR (m.category IS NULL AND :category IS NULL)) " +
            "AND m.deleted = false")
    void shiftSortOrderDown(@Param("sortOrder") Long sortOrder, @Param("category") Category category);

    List<Menu> findAllByCategory(Category category);
}

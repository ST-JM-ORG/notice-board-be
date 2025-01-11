package com.notice_board.repository;

import com.notice_board.model.menu.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findTopByOrderBySortOrderDesc();

    @Modifying
    @Query("UPDATE Category c SET c.sortOrder = c.sortOrder - 1 WHERE c.sortOrder > :sortOrder AND c.deleted = false")
    void shiftSortOrderDown(@Param("sortOrder") Long sortOrder);
}

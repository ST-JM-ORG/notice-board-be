package com.notice_board.repository;

import com.notice_board.model.menu.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findTopByOrderBySortOrderDesc();
}

package com.notice_board.repository;

import com.notice_board.model.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuRepository extends JpaRepository<Menu, Long> {

}

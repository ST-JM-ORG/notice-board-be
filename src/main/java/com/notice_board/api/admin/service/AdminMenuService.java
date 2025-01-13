package com.notice_board.api.admin.service;

import com.notice_board.api.admin.dto.MenuDto;
import com.notice_board.api.admin.vo.MenuVo;

public interface AdminMenuService {
    void checkMenuCode(String menuCode);

    void createMenu(MenuDto menuDto);

    MenuVo getMenuDetail(Long id);
}

package com.notice_board.api.admin.service;

import com.notice_board.api.admin.dto.MenuDto;

public interface AdminMenuService {

    void checkMenuCode(String menuCode);

    void createMenu(MenuDto menuDto);
}

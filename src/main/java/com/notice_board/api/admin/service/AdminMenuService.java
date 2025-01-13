package com.notice_board.api.admin.service;

import com.notice_board.api.admin.dto.MenuDto;
import com.notice_board.api.admin.dto.MenuSearchReqDto;
import com.notice_board.api.admin.dto.MenuSortDto;
import com.notice_board.api.admin.vo.MenuVo;

import java.util.List;

public interface AdminMenuService {
    void checkMenuCode(String menuCode);

    void createMenu(MenuDto menuDto);

    MenuVo getMenuDetail(Long id);

    void modifyMenu(MenuDto menuDto, Long id);

    void deleteMenu(Long id);

    void changeSortOrder(MenuSortDto menuSortDto);

    List<MenuVo> getMenuList(MenuSearchReqDto reqDto);
}

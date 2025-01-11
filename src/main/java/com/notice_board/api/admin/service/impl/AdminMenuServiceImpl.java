package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.MenuDto;
import com.notice_board.api.admin.service.AdminMenuService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.common.exception.ValidException;
import com.notice_board.model.menu.Menu;
import com.notice_board.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service("adminMenuService")
@RequiredArgsConstructor
@Transactional
public class AdminMenuServiceImpl implements AdminMenuService {

    private final MenuRepository menuRepository;

    private final ModelMapper modelMapper;

    @Override
    public void checkMenuCode(String menuCode) {
        if (StringUtils.isBlank(menuCode)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuCode", "메뉴 코드를 입력해주세요.");
        }

        Optional<Menu> menu = menuRepository.findByMenuCode(menuCode);

        if (menu.isPresent()) {
            throw new ValidException(CommonExceptionResultMessage.DUPLICATE_FAIL, "menuCode", "사용 중인 메뉴 코드 입니다.");
        }
    }

    @Override
    public void createMenu(MenuDto menuDto) {
        this.checkMenuCode(menuDto.getMenuCode());

        if (StringUtils.isBlank(menuDto.getMenuNm())) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuNm", "메뉴 이름을 입력해주세요.");
        }

        long menuOrder = 1;

        Optional<Menu> topOrderBy = menuRepository.findTopByOrderByMenuOrderDesc();
        if(topOrderBy.isPresent()) {
            menuOrder = topOrderBy.get().getMenuOrder() + 1;
        }

        Menu menu = modelMapper.map(menuDto, Menu.class);
        menu.setMenuOrder(menuOrder);

        menuRepository.save(menu);

        if (menu.getId() == null) {
            throw new CustomException(CommonExceptionResultMessage.DB_FAIL);
        }
    }
}

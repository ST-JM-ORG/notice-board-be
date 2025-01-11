package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.service.AdminMenuService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.ValidException;
import com.notice_board.model.menu.Menu;
import com.notice_board.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service("adminMenuService")
@RequiredArgsConstructor
@Transactional
public class AdminMenuServiceImpl implements AdminMenuService {

    private final MenuRepository menuRepository;

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
}

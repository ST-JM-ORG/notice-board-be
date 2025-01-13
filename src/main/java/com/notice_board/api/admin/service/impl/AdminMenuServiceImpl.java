package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.MenuDto;
import com.notice_board.api.admin.service.AdminMenuService;
import com.notice_board.api.admin.vo.MenuVo;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.common.exception.ValidException;
import com.notice_board.model.menu.Category;
import com.notice_board.model.menu.Menu;
import com.notice_board.repository.CategoryRepository;
import com.notice_board.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;


@Service("adminMenuService")
@RequiredArgsConstructor
@Transactional
public class AdminMenuServiceImpl implements AdminMenuService {

    private final MenuRepository menuRepository;

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    @Override
    public void checkMenuCode(String menuCode) {
        this.validMenuCode(menuCode);

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

        Long categoryId = menuDto.getCategoryId();
        if (categoryId == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "categoryId", "카테고리를 선택해주세요.");
        }

        Category category = null;
        long sortOrder;

        if (categoryId != 0) { // 미선택이 아닐 경우
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "카테고리 조회 실패: ID " + categoryId + "에 해당하는 카테고리 없음"));

            sortOrder = category.getMenuList().stream()
                    .map(Menu::getSortOrder)
                    .max(Comparator.naturalOrder())
                    .orElse(0L) + 1;
        } else { // 카테고리 미선택
            sortOrder = menuRepository.findTopByCategoryIsNullOrderBySortOrderDesc()
                    .map(Menu::getSortOrder)
                    .orElse(0L) + 1;
        }

        Menu menu = modelMapper.map(menuDto, Menu.class);
        menu.setSortOrder(sortOrder);
        menu.setCategory(category);

        menuRepository.save(menu);

        if (menu.getId() == null) {
            throw new CustomException(CommonExceptionResultMessage.DB_FAIL);
        }
    }

    @Override
    public MenuVo getMenuDetail(Long id) {
        Menu menu = this.getMenu(id);
        return MenuVo.toVO(menu);
    }

    @Override
    public void modifyMenu(MenuDto menuDto, Long id) {
        String newMenuCode = menuDto.getMenuCode();
        this.validMenuCode(newMenuCode);

        if (StringUtils.isBlank(menuDto.getMenuNm())) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuNm", "메뉴 이름을 입력해주세요.");
        }

        Long newCategoryId = menuDto.getCategoryId();
        if (newCategoryId == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "categoryId", "카테고리를 선택해주세요.");
        }

        // 기존 메뉴 조회
        Menu menu = this.getMenu(id);
        Category originCategory = menu.getCategory();

        String originMenuCode = menu.getMenuCode();
        Long originCategoryId = originCategory == null ? 0 : originCategory.getId();

        menu.setMenuNm(menuDto.getMenuNm());

        // 메뉴 코드가 바꼈을 경우
        if (!StringUtils.equals(originMenuCode, newMenuCode)) {
            this.checkMenuCode(newMenuCode);
            menu.setMenuCode(newMenuCode);
        }

        // 카테고리가 변경되었을 경우
        if (!originCategoryId.equals(newCategoryId)) {
            Category category = null;
            long originSortOrder = menu.getSortOrder();
            long sortOrder;

            if (newCategoryId != 0) { // 미선택이 아닐 경우
                category = categoryRepository.findById(newCategoryId)
                        .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "카테고리 조회 실패: ID " + newCategoryId + "에 해당하는 카테고리 없음"));

                sortOrder = category.getMenuList().stream()
                        .map(Menu::getSortOrder)
                        .max(Comparator.naturalOrder())
                        .orElse(0L) + 1;
            } else { // 카테고리 미선택
                sortOrder = menuRepository.findTopByCategoryIsNullOrderBySortOrderDesc()
                        .map(Menu::getSortOrder)
                        .orElse(0L) + 1;
            }

            menu.setSortOrder(sortOrder);
            menu.setCategory(category);

            // 기존 카테고리에서 메뉴보다 sortOrder 가 큰 메뉴들의 sortOrder 를 1씩 줄이기
            menuRepository.shiftSortOrderDown(originSortOrder, originCategory);
        }

        menu.setSummary(menuDto.getSummary());

        menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        Menu menu = this.getMenu(id);

        Category category = menu.getCategory();
        Long targetSortOrder = menu.getSortOrder();

        // 기존 카테고리에서 메뉴보다 sortOrder 가 큰 메뉴들의 sortOrder 를 1씩 줄이기
        menuRepository.shiftSortOrderDown(targetSortOrder, category);

        menuRepository.delete(menu);
    }

    private void validMenuCode (String menuCode) {
        if (StringUtils.isBlank(menuCode)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuCode", "메뉴 코드를 입력해주세요.");
        }

        if (!menuCode.matches("^[a-zA-Z_]+$")) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuCode", "메뉴 코드는 영어와 '_'만 사용할 수 있습니다");
        }
    }

    private Menu getMenu(Long id) {
        if (id == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "메뉴 ID를 입력해주세요.");
        }

        return menuRepository.findById(id)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "메뉴 조회 실패: ID " + id + "에 해당하는 메뉴 없음"));
    }
}

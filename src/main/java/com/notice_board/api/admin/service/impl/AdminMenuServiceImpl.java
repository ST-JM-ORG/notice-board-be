package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.*;
import com.notice_board.api.admin.service.AdminMenuService;
import com.notice_board.api.admin.vo.CategoryVo;
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

import java.util.*;
import java.util.stream.Collectors;


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

    @Override
    public void changeSortOrder(MenuSortDto menuSortDto) {
        Long categoryId = menuSortDto.getCategoryId();

        if (categoryId == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "categoryId", "카테고리 ID를 입력해주세요.");
        }

        Set<Long> sortOrderSet = new HashSet<>();
        List<MenuSortListDto> menuSortList = menuSortDto.getMenuSortList();

        List<Menu> menuList = this.getMenuListByCategoryId(categoryId);
        if (!menuSortList.stream().allMatch(dto -> menuList.stream().anyMatch(menu -> menu.getId().equals(dto.getId())))) {
            throw new CustomException(CommonExceptionResultMessage.VALID_FAIL, "메뉴 데이터가 일치하지 않습니다.");
        }

        for (MenuSortListDto data : menuSortList) {
            Long id = data.getId();
            Long sortOrder = data.getSortOrder();

            if (id == null) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "메뉴 ID를 입력해주세요.");
            }

            if (sortOrder == null) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "sortOrder", "정렬 순서를 입력해주세요.");
            }

            if (sortOrder > menuList.size() || sortOrder < 1) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "sortOrder", "올바른 정렬 순서를 입력해주세요.");
            }

            if (sortOrderSet.contains(sortOrder)) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "sortOrder", sortOrder + "는 중복된 정렬 순서 입니다.");
            }

            Menu menu = menuList.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND,
                            "메뉴 조회 실패: ID " + id + "에 해당하는 메뉴 없음"));

            menu.setSortOrder(sortOrder);
            sortOrderSet.add(sortOrder);
        }

        menuRepository.saveAll(menuList);
    }

    @Override
    public List<MenuVo> getMenuList(MenuSearchReqDto reqDto) {
        Long categoryId = reqDto.getCategoryId();

        List<Menu> menuList = this.getMenuListByCategoryId(categoryId);

        menuList.sort(Comparator.comparing(Menu::getSortOrder));

        return menuList.stream()
                .map(MenuVo::toVO)
                .collect(Collectors.toList());
    }

    private void validMenuCode(String menuCode) {
        if (StringUtils.isBlank(menuCode)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuCode", "메뉴 코드를 입력해주세요.");
        }

        if (!menuCode.matches("^[a-zA-Z_]+$")) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "menuCode", "메뉴 코드는 영어와 '_'만 사용할 수 있습니다");
        }
    }

    private List<Menu> getMenuListByCategoryId(Long categoryId) {
        // categoryId가 null인 경우, 모든 메뉴를 가져옴
        if (categoryId == null) {
            return menuRepository.findAll();
        }

        // categoryId가 0인 경우, category가 null인 메뉴를 가져옴
        if (categoryId == 0) {
            return menuRepository.findAllByCategory(null);
        }

        // categoryId가 유효한 경우, 해당 카테고리의 메뉴를 가져옴
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND,
                        "카테고리 조회 실패: ID " + categoryId + "에 해당하는 카테고리 없음"));
        return category.getMenuList();
    }

    private Menu getMenu(Long id) {
        if (id == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "메뉴 ID를 입력해주세요.");
        }

        return menuRepository.findById(id)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "메뉴 조회 실패: ID " + id + "에 해당하는 메뉴 없음"));
    }
}

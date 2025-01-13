package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.CategoryDto;
import com.notice_board.api.admin.dto.CategorySortDto;
import com.notice_board.api.admin.service.AdminCategoryService;
import com.notice_board.api.admin.vo.CategoryVo;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import com.notice_board.common.exception.ValidException;
import com.notice_board.model.menu.Category;
import com.notice_board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service("adminCategoryService")
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<CategoryVo> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAllByOrderBySortOrderAsc();

        return categoryList.stream()
                .map(c -> CategoryVo.toVO(c))
                .collect(Collectors.toList());
    }

    @Override
    public void createCategory(CategoryDto categoryDto) {

        if (StringUtils.isBlank(categoryDto.getCategoryNm())) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "categoryNm", "카테고리 명을 입력해주세요.");
        }

        long sortOrder = categoryRepository.findTopByOrderBySortOrderDesc()
                .map(Category::getSortOrder)
                .orElse(0L) + 1;

        Category category = modelMapper.map(categoryDto, Category.class);
        category.setSortOrder(sortOrder);

        categoryRepository.save(category);

        if (category.getId() == null) {
            throw new CustomException(CommonExceptionResultMessage.DB_FAIL);
        }
    }

    @Override
    public CategoryVo getCategoryDetail(Long id) {
        Category category = this.getCategory(id);
        return CategoryVo.toVO(category);
    }

    @Override
    public void modifyCategory(CategoryDto categoryDto, Long id) {
        String categoryNm = categoryDto.getCategoryNm();
        if (StringUtils.isBlank(categoryNm)) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "categoryNm", "카테고리 명을 입력해주세요.");
        }

        Category category = this.getCategory(id);
        category.setCategoryNm(categoryNm);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = this.getCategory(id);

        if (!category.getMenuList().isEmpty()) {
            throw new CustomException(CommonExceptionResultMessage.VALID_FAIL, "카테고리를 사용 중인 메뉴가 있습니다.");
        }

        Long targetSortOrder = category.getSortOrder();

        // 해당 카테고리보다 sortOrder 가 큰 카테고리들의 sortOrder 를 1씩 줄이기
        categoryRepository.shiftSortOrderDown(targetSortOrder);

        categoryRepository.delete(category);
    }

    @Override
    public void changeSortOrder(List<CategorySortDto> list) {
        List<Category> categoryList = categoryRepository.findAll();

        Set<Long> sortOrderSet = new HashSet<>();

        if (categoryList.size() != list.size()) {
            throw new CustomException(CommonExceptionResultMessage.VALID_FAIL, "모든 카테고리 데이터를 입력해주세요.");
        }

        for (CategorySortDto ct : list) {
            Long id = ct.getId();
            Long sortOrder = ct.getSortOrder();

            if (id == null) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "카테고리 ID를 입력해주세요.");
            }

            if (sortOrder == null) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "sortOrder", "정렬 순서를 입력해주세요.");
            }

            if (sortOrder > categoryList.size() || sortOrder < 1) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "sortOrder", "올바른 정렬 순서를 입력해주세요.");
            }

            if (sortOrderSet.contains(sortOrder)) {
                throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "sortOrder", sortOrder + "는 중복된 정렬 순서 입니다.");
            }

            Category category = categoryList.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND,
                            "카테고리 조회 실패: ID " + id + "에 해당하는 카테고리 없음"));

            category.setSortOrder(sortOrder);
            sortOrderSet.add(sortOrder);
        }

        categoryRepository.saveAll(categoryList);
    }

    private Category getCategory(Long id) {
        if (id == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "카테고리 ID를 입력해주세요.");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "카테고리 조회 실패: ID " + id + "에 해당하는 카테고리 없음"));
    }
}

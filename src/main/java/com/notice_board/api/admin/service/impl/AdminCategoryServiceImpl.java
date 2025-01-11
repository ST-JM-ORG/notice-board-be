package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.CategoryDto;
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

import java.util.Optional;


@Service("adminCategoryService")
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public void createCategory(CategoryDto categoryDto) {

        if (StringUtils.isBlank(categoryDto.getCategoryNm())) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "categoryNm", "카테고리 명을 입력해주세요.");
        }

        long sortOrder = 1;
        Optional<Category> topOrderBy = categoryRepository.findTopByOrderBySortOrderDesc();
        if (topOrderBy.isPresent()) {
            sortOrder = topOrderBy.get().getSortOrder() + 1;
        }

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

    private Category getCategory(Long id) {
        if (id == null) {
            throw new ValidException(CommonExceptionResultMessage.VALID_FAIL, "id", "카테고리 PK를 입력해주세요.");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(CommonExceptionResultMessage.NOT_FOUND, "카테고리 조회 실패: ID " + id + "에 해당하는 카테고리 없음"));
    }
}

package com.notice_board.api.admin.service.impl;

import com.notice_board.api.admin.dto.CategoryDto;
import com.notice_board.api.admin.service.AdminCategoryService;
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
}

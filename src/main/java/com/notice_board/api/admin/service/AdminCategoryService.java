package com.notice_board.api.admin.service;


import com.notice_board.api.admin.dto.CategoryDto;
import com.notice_board.api.admin.dto.CategorySortDto;
import com.notice_board.api.admin.vo.CategoryVo;

import java.util.List;

public interface AdminCategoryService {

    List<CategoryVo> getCategoryList();

    void createCategory(CategoryDto categoryDto);

    CategoryVo getCategoryDetail(Long id);

    void modifyCategory(CategoryDto categoryDto, Long id);

    void deleteCategory(Long id);

    void changeSortOrder(List<CategorySortDto> list);
}

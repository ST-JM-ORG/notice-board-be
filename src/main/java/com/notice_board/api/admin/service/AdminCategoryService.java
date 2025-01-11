package com.notice_board.api.admin.service;


import com.notice_board.api.admin.dto.CategoryDto;
import com.notice_board.api.admin.vo.CategoryVo;

public interface AdminCategoryService {

    void createCategory(CategoryDto categoryDto);

    CategoryVo getCategoryDetail(Long id);

    void modifyCategory(CategoryDto categoryDto, Long id);
}

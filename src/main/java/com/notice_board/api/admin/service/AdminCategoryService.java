package com.notice_board.api.admin.service;


import com.notice_board.api.admin.dto.CategoryDto;

public interface AdminCategoryService {

    void createCategory(CategoryDto categoryDto);

    void modifyCategory(CategoryDto categoryDto, Long id);

}

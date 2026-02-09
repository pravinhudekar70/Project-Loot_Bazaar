package com.ecom.service;

import com.ecom.payload.CategoryDTO;
import com.ecom.payload.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageName, Integer pageSize,String sortBy, String sortOrder);
    CategoryDTO createCategories(CategoryDTO categoryDTO);
    CategoryDTO deleteCategories(long categoryId);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}

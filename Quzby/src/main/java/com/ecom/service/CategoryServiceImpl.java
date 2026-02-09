package com.ecom.service;

import com.ecom.exceptions.ApiException;
import com.ecom.exceptions.ResourceNotFoundException;
import com.ecom.model.Category;
import com.ecom.payload.CategoryDTO;
import com.ecom.payload.CategoryResponse;
import com.ecom.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageName,Integer pageSize, String sortBy, String sortOrder ) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ase")?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageName,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        List<Category> categoryList = categoryPage.getContent();
        if (categoryList.isEmpty()) {
            throw new ApiException("No categories found");
        }
        List<CategoryDTO> categoryDTOS = categoryList.stream()
                .map(category->modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageName(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategories(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category savedCategoryDB = categoryRepo.findByCategoryName(category.getCategoryName());
        if(savedCategoryDB != null){
            throw new ApiException("Category with the name "+category.getCategoryName()+ " already exits !!!");
        }
       Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategories(long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                        .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        categoryRepo.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO ) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());
        if(savedCategory != null){
            throw new ApiException("Category with the name "+category.getCategoryName()+ " already exits !!!");
        }
        Category oldCategory = categoryRepo.findById(categoryId)
                .orElseThrow(()->  new ResourceNotFoundException("Category","CategoryId",categoryId));
        oldCategory.setCategoryName(category.getCategoryName());
          return modelMapper.map(categoryRepo.save(oldCategory),CategoryDTO.class);
    }
}

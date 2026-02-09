package com.ecom.service;

import com.ecom.payload.ProductDTO;
import com.ecom.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse getAllProducts(Integer pageName, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getAllProductByCategory(Long categoryId, Integer pageName, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeyword(String keyword,Integer pageName, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);
}

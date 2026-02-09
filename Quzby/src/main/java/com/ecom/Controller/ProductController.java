package com.ecom.Controller;

import com.ecom.config.AppConstant;
import com.ecom.model.Product;
import com.ecom.payload.ProductDTO;
import com.ecom.payload.ProductResponse;
import com.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
      return new ResponseEntity<>(productService.addProduct(productDTO,categoryId), HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct( @RequestParam(name="pageName",defaultValue = AppConstant.PAGE_NUMBER, required = false)Integer pageName,
                                                          @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy",defaultValue = AppConstant.SORTBY_PRICE,required = false)String sortBy,
                                                          @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORTORDER_ASE, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageName,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductByCategory(@PathVariable Long categoryId,
                                                          @RequestParam(name="pageName",defaultValue = AppConstant.PAGE_NUMBER, required = false)Integer pageName,
                                                          @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy",defaultValue = AppConstant.SORTBY_PRICE,required = false)String sortBy,
                                                          @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORTORDER_ASE, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProductByCategory(categoryId,pageName,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
                                                                  @RequestParam(name="pageName",defaultValue = AppConstant.PAGE_NUMBER, required = false)Integer pageName,
                                                                  @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                  @RequestParam(name = "sortBy",defaultValue = AppConstant.SORTBY_PRICE,required = false)String sortBy,
                                                                  @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORTORDER_ASE, required = false) String sortOrder)
            {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword,pageName,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,@PathVariable Long productId){

        return new ResponseEntity<>(productService.updateProduct(productDTO,productId),HttpStatus.OK);
    }
    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){

        return new ResponseEntity<>(productService.deleteProduct(productId),HttpStatus.OK);
    }
}

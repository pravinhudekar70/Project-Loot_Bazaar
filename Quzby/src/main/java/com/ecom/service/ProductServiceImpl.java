package com.ecom.service;

import com.ecom.exceptions.ApiException;
import com.ecom.exceptions.ResourceNotFoundException;
import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.payload.ProductDTO;
import com.ecom.payload.ProductResponse;
import com.ecom.repository.CategoryRepo;
import com.ecom.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
       Category category = categoryRepo.findById(categoryId).orElseThrow(()->
               new ResourceNotFoundException("Category","CategoryId",categoryId));

        Product product = modelMapper.map(productDTO,Product.class);
        boolean isProductPresent = productRepo.existsByProductNameIgnoreCase(product.getProductName());
        if(!isProductPresent){
            if(product.getCategory()!=category){
                product.setCategory(category);
            }
            product.setImage("default.png");
            double specialPrice = product.getPrice()-((product.getDiscount()*0.01)* product.getPrice());
            product.setSpecialPrice(specialPrice);
            return modelMapper.map(productRepo.save(product),ProductDTO.class);
        } else {
            throw new ApiException("Product is already exists !!!");
        }

    }

    @Override
    public ProductResponse getAllProducts(Integer pageName, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ase")?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageName,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepo.findAll(pageable);
        List<Product> productList = productPage.getContent();
        if(productList.isEmpty()){
            throw new ApiException("Product not found!!");
        }
        List<ProductDTO> productDTOList = productList.stream()
                .map(p-> modelMapper.map(p,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageName(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductByCategory(Long categoryId, Integer pageName, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ase")?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageName,pageSize,sortByAndOrder);
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->
                new ResourceNotFoundException("Category","CategoryId",categoryId));
        Page<Product> productPage =productRepo.findAllByCategoryCategoryId(categoryId,pageable);
        List<Product> productList = productPage.getContent();
        if(productList.isEmpty()){
            throw new ApiException("Product is not found!!");
        }
        List<ProductDTO> productDTOList = productList.stream().map(pl->
                modelMapper.map(pl,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageName(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());


        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword,Integer pageName, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ase")?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageName,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepo.findByProductNameContainingIgnoreCase(keyword,pageable);
        List<Product> productList = productPage.getContent();
        if(productList.isEmpty()){
            throw new ApiException("Product not found!!");
        }
        List<ProductDTO> productDTOList = productList.stream()
                .map(p-> modelMapper.map(p,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageName(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("product","productId",productId));
        ProductDTO savedProductDTO = modelMapper.map(product,ProductDTO.class);
        savedProductDTO.setProductName(productDTO.getProductName());
        savedProductDTO.setProductDescription(productDTO.getProductDescription());
        savedProductDTO.setQuantity(productDTO.getQuantity());
        savedProductDTO.setImage(productDTO.getImage());
        savedProductDTO.setPrice(productDTO.getPrice());
        savedProductDTO.setDiscount(productDTO.getDiscount());
        double specialPrice = productDTO.getPrice()-((productDTO.getDiscount()*0.01)* productDTO.getPrice());
        savedProductDTO.setSpecialPrice(specialPrice);
        Product productNew = productRepo.save(modelMapper.map(savedProductDTO, Product.class));
        return modelMapper.map(productNew,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()->
             new ResourceNotFoundException("product","productId",productId));
        productRepo.delete(product);
        return modelMapper.map(product,ProductDTO.class) ;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepo.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("product","productId",productId));
                    String fileName = fileService.uploadImage(path,image);
                    product.setImage(fileName);
                    Product updatedProduct = productRepo.save(product);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

}

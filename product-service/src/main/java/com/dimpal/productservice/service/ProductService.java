package com.dimpal.productservice.service;

import com.dimpal.productservice.dto.ProductRequest;
import com.dimpal.productservice.dto.ProductResponse;
import com.dimpal.productservice.model.Product;
import com.dimpal.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
/**
 * @RequiredArgsConstructor will create all required constructor (during runtime) which can be constructor of
 * productRepository or any other
 */
@Slf4j
/**
 * to use logger
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /*public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }*/
    //no need of above since we have used @RequiredArgsConstructor annotation

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                                 .name(productRequest.getName())
                                 .description(productRequest.getDescription())
                                 .price(productRequest.getPrice())
                                 .build();

        productRepository.save(product);
        log.info("product {} is saved" + product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
         return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
        // products.stream().map(product -> mapToProductResponse(product));
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}

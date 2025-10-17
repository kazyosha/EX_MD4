package com.c05.kaz.ex_module4.service;

import com.c05.kaz.ex_module4.model.Category;
import com.c05.kaz.ex_module4.model.Product;
import com.c05.kaz.ex_module4.model.dto.ProductDTO;
import com.c05.kaz.ex_module4.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product save(ProductDTO dto) {
        Category cat = categoryService.findById(dto.getCategoryId());
        Product p = Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .status(dto.getStatus() != null ? dto.getStatus() : "chờ duyệt")
                .category(cat)
                .build();
        return productRepository.save(p);
    }

    public Product update(Long id, ProductDTO dto) {
        Product existing = findById(id);
        if (existing == null) return null;

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setStatus(dto.getStatus());
        existing.setCategory(categoryService.findById(dto.getCategoryId()));

        return productRepository.save(existing);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        productRepository.deleteAllById(ids);
    }
    public Page<Product> search(String name, Long categoryId, Double minPrice, Pageable pageable) {
        String qName = (name == null || name.isBlank()) ? null : name.trim();
        return productRepository.search(qName, categoryId, minPrice, pageable);
    }

    public ProductDTO toDTO(Product p) {
        if (p == null) return null;
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .status(p.getStatus())
                .categoryId(p.getCategory().getCid())
                .build();
    }
}

package com.c05.kaz.ex_module4.repository;

import com.c05.kaz.ex_module4.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            SELECT p FROM Product p
            WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:categoryId IS NULL OR p.category.cid = :categoryId)
            AND (:minPrice IS NULL OR p.price >= :minPrice)
            """)
    Page<Product> search(@Param("name") String name,
                         @Param("categoryId") Long categoryId,
                         @Param("minPrice") Double minPrice,
                         Pageable pageable);
}

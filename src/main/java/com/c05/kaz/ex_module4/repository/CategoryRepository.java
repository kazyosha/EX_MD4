package com.c05.kaz.ex_module4.repository;

import com.c05.kaz.ex_module4.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

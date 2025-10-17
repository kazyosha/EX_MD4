package com.c05.kaz.ex_module4.controller;

import com.c05.kaz.ex_module4.model.Product;
import com.c05.kaz.ex_module4.model.dto.ProductDTO;
import com.c05.kaz.ex_module4.service.CategoryService;
import com.c05.kaz.ex_module4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("/auction/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    private static final int PAGE_SIZE = 5;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(Math.max(0, page), PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        Page<Product> productPage = productService.search(name, categoryId, minPrice, pageable);

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products", productPage);
        model.addAttribute("name", name);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("categoryId", categoryId);
        return "product/list";
    }

    @PostMapping("/delete")
    public String deleteSelected(
            @RequestParam(name = "ids", required = false) List<Long> selectedIds,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", required = false) Integer page
    ) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
            productService.deleteByIds(selectedIds);
        }
        UriComponentsBuilder uri = UriComponentsBuilder.fromPath("/auction/products");
        if (name != null) uri.queryParam("name", name);
        if (minPrice != null) uri.queryParam("minPrice", minPrice);
        if (categoryId != null) uri.queryParam("categoryId", categoryId);
        if (page != null) uri.queryParam("page", page);
        return "redirect:" + uri.toUriString();
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.findAll());
        return "product/create";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") ProductDTO dto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product/create";
        }
        productService.save(dto);
        return "redirect:/auction/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }

        ProductDTO dto = productService.toDTO(product);
        model.addAttribute("productDTO", dto);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("statuses", new String[]{"chờ duyệt", "đang đấu giá", "đã bán"});
        return "product/edit";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("productDTO") ProductDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("statuses", new String[]{"chờ duyệt", "đang đấu giá", "đã bán"});
            return "product/edit";
        }

        productService.update(dto.getId(), dto);
        return "redirect:/auction/products";
    }
}

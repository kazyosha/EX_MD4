package com.c05.kaz.ex_module4.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 5, max = 50, message = "Tên sản phẩm phải từ 5 đến 50 ký tự")
    private String name;

    @NotNull(message = "Giá khởi điểm không được để trống")
    @Min(value = 100000, message = "Giá khởi điểm phải ít nhất là 100.000 VND")
    private Double price;

//    @NotBlank(message = "Tình trạng không được để trống")
    private String status;

    @NotNull(message = "Loại sản phẩm không được để trống")
    private Long categoryId;
}

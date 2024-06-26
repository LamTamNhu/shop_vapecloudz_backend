package com.shop_vapecloudz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartEditDTO {
    private Long variantId;
    private Long cartId;
    private Integer amount;
    private String email;
}

package com.shop_vapecloudz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayPalAccessTokenDTO {
    private String scope;
    private String access_token;
    private String token_type;
    private String app_id;
    private Long expires_in;
    private String nonce;
}

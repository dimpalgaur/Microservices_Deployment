package com.dimpal.productservice.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * ProductResponse class : we created this class as it is good practice to
 * separate your model class and DTO class because we should not expose your model class
 * to the user, in case in future if we have added 2 more property into our model class
 * it will also come when we return response that's why, in ProductResponse class we
 * can only return the property we want to provide.
 */
public class ProductResponse {
        private String id;
        private String name;
        private String description;
        private BigDecimal price;
}

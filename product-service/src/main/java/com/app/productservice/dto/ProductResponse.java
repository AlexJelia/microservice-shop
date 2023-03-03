package com.app.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/*it is a good practice to separate model entities and dtos
you should not expose your model entities to the outside world
Ex: what if we add two new business-required fields to the model
that are not necessary outside?*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}

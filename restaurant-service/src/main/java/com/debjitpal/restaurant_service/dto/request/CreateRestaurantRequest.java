package com.debjitpal.restaurant_service.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantRequest {

    @NotBlank
    private String name;
    private String address;
    private String city;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double rating;

    private Boolean isOpen;


}

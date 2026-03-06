package com.debjitpal.menu_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuItemRequest {

    @NotNull
    private UUID restaurantId;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Positive
    private Double price;

    private Boolean isAvailable;

}

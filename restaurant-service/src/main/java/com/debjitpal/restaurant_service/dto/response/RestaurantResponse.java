package com.debjitpal.restaurant_service.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@JsonPropertyOrder({
        "id",
        "name",
        "address",
        "city",
        "rating",
        "isOpen"
})
@Getter
@Setter
@Builder
public class RestaurantResponse {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private Double rating;
    private Boolean isOpen;
}

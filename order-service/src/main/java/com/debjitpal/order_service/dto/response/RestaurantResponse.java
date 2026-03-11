package com.debjitpal.order_service.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponse {
    private UUID id;
    private String name;
    private String address;
    private String city;
    private Double rating;
    private Boolean isOpen;
}

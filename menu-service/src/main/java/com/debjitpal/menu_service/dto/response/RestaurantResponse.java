package com.debjitpal.menu_service.dto.response;

import jdk.jshell.Snippet;
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


    public RestaurantResponse(UUID restaurantId, String theRestaurantHouse, boolean b) {
    }


}

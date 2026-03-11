package com.debjitpal.order_service.external.service;

import com.debjitpal.order_service.config.RestaurantClientFallback;
import com.debjitpal.order_service.dto.response.ApiResponse;
import com.debjitpal.order_service.dto.response.RestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "RESTAURANT-SERVICE",
        path = "/api/restaurants",
        fallback = RestaurantClientFallback.class
)
public interface RestaurantClient {

    @GetMapping("/{id}")
    ApiResponse<RestaurantResponse> getRestaurantById(@PathVariable UUID id);
}

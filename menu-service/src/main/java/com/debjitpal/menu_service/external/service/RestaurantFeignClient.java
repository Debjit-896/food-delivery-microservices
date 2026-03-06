package com.debjitpal.menu_service.external.service;

import com.debjitpal.menu_service.dto.response.ApiResponse;
import com.debjitpal.menu_service.dto.response.RestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "RESTAURANT-SERVICE")
public interface RestaurantFeignClient {

    @GetMapping("/api/restaurants/{id}")
    ApiResponse<RestaurantResponse> getRestaurantById(@PathVariable UUID id);
}

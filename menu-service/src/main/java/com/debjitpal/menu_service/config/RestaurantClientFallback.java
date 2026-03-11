package com.debjitpal.menu_service.config;

import com.debjitpal.menu_service.dto.response.ApiResponse;
import com.debjitpal.menu_service.dto.response.RestaurantResponse;
import com.debjitpal.menu_service.external.service.RestaurantFeignClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantClientFallback implements RestaurantFeignClient {

    @Override
    public ApiResponse<RestaurantResponse> getRestaurantById(UUID id) {
        return ApiResponse.failure(
                "Restaurant Service is currently unavailable. Please try again later.", null
        );
    }
}

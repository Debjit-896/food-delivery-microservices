package com.debjitpal.order_service.config;

import com.debjitpal.order_service.dto.response.ApiResponse;
import com.debjitpal.order_service.dto.response.RestaurantResponse;
import com.debjitpal.order_service.external.service.RestaurantClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantClientFallback implements RestaurantClient {

    @Override
    public ApiResponse<RestaurantResponse> getRestaurantById(UUID id) {
        return ApiResponse.failure("Restaurant Service is currently unavailable. Please try again later.", null);
    }
}

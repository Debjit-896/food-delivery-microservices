package com.debjitpal.order_service.config;

import com.debjitpal.order_service.dto.response.ApiResponse;
import com.debjitpal.order_service.dto.response.MenuItemResponse;
import com.debjitpal.order_service.external.service.MenuClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MenuClientFallback implements MenuClient {

    @Override
    public ApiResponse<MenuItemResponse> getMenuItemById(UUID id) {
        return ApiResponse.failure("Menu Service is currently unavailable. Please try again later.", null);
    }
}

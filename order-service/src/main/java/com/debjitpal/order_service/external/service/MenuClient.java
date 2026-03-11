package com.debjitpal.order_service.external.service;

import com.debjitpal.order_service.config.MenuClientFallback;
import com.debjitpal.order_service.dto.response.ApiResponse;
import com.debjitpal.order_service.dto.response.MenuItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "MENU-SERVICE",
        path = "/api/menu",
        fallback = MenuClientFallback.class
)
public interface MenuClient {

    @GetMapping("/{id}")
    ApiResponse<MenuItemResponse> getMenuItemById(@PathVariable UUID id);
}

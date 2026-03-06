package com.debjitpal.menu_service.service;

import com.debjitpal.menu_service.dto.request.CreateMenuItemRequest;
import com.debjitpal.menu_service.dto.response.MenuItemResponse;

import java.util.List;
import java.util.UUID;

public interface MenuItemService {
    MenuItemResponse createMenuItem(CreateMenuItemRequest menuItemRequest);
    MenuItemResponse updateMenuItem(UUID id, CreateMenuItemRequest menuItemRequest);
    List<MenuItemResponse> getAllMenuItem();
    MenuItemResponse getMenuItemById(UUID id);
    MenuItemResponse updatePrice(UUID id, Double price);
    MenuItemResponse enableDisableMenuItem(UUID id, Boolean isAvailable);
    List<MenuItemResponse> getMenuByRestaurantId(UUID restaurantId);
    void deleteMenuItem(UUID id);
}

package com.debjitpal.menu_service.service.impl;

import com.debjitpal.menu_service.dto.request.CreateMenuItemRequest;
import com.debjitpal.menu_service.dto.response.ApiResponse;
import com.debjitpal.menu_service.dto.response.MenuItemResponse;
import com.debjitpal.menu_service.dto.response.RestaurantResponse;
import com.debjitpal.menu_service.entity.MenuItem;
import com.debjitpal.menu_service.exception.MenuItemNotFoundException;
import com.debjitpal.menu_service.exception.RestaurantClosedException;
import com.debjitpal.menu_service.external.service.RestaurantFeignClient;
import com.debjitpal.menu_service.repository.MenuItemRepository;
import com.debjitpal.menu_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantFeignClient restaurantFeignClient;

    @Override
    public MenuItemResponse createMenuItem(CreateMenuItemRequest menuItemRequest) {
        MenuItem item = MenuItem.builder()
                .restaurantId(menuItemRequest.getRestaurantId())
                .name(menuItemRequest.getName())
                .description(menuItemRequest.getDescription())
                .price(menuItemRequest.getPrice())
                .isAvailable(menuItemRequest.getIsAvailable())
                .build();

        return mapToResponse(menuItemRepository.save(item));
    }

    @Override
    public MenuItemResponse updateMenuItem(UUID id, CreateMenuItemRequest menuItemRequest) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException
                        ("Menu item not found with id : " +id)
                );

        item.setRestaurantId(menuItemRequest.getRestaurantId());
        item.setName(menuItemRequest.getName());
        item.setDescription(menuItemRequest.getDescription());
        item.setPrice(menuItemRequest.getPrice());
        item.setIsAvailable(menuItemRequest.getIsAvailable());

        return mapToResponse(menuItemRepository.save(item));
    }

    @Override
    public List<MenuItemResponse> getAllMenuItem() {
        List<MenuItem> menuItems = menuItemRepository.findAll();

        if (menuItems.isEmpty()){
            throw new MenuItemNotFoundException("No menu item available");
        }

        return menuItems.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MenuItemResponse getMenuItemById(UUID id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException
                        ("Menu item not found with id: " +id)
                );

        return mapToResponse(menuItem);
    }

    @Override
    public MenuItemResponse updatePrice(UUID id, Double price) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: "+id));

        item.setPrice(price);

        return mapToResponse(menuItemRepository.save(item));
    }

    @Override
    public MenuItemResponse enableDisableMenuItem(UUID id, Boolean isAvailable) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " +id));

        item.setIsAvailable(isAvailable);

        return mapToResponse(menuItemRepository.save(item));
    }

    @Override
    public List<MenuItemResponse> getMenuByRestaurantId(UUID restaurantId) {

        ApiResponse<RestaurantResponse> response = restaurantFeignClient.getRestaurantById(restaurantId);

        RestaurantResponse restaurant = response.getData();

        if (!Boolean.TRUE.equals(restaurant.getIsOpen())) {
            throw new RuntimeException("Restaurant is closed or unavailable");
        }

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteMenuItem(UUID id) {
        MenuItem item =menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " +id));

        menuItemRepository.delete(item);
        log.warn("Menu item deleted - id: {} name: {}", id,item.getName());
    }

    public MenuItemResponse mapToResponse(MenuItem item){
        return MenuItemResponse.builder()
                .id(item.getId())
                .restaurantId(item.getRestaurantId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .isAvailable(item.getIsAvailable())
                .build();
    }
}

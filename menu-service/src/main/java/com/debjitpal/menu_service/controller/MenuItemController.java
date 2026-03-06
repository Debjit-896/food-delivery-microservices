package com.debjitpal.menu_service.controller;

import com.debjitpal.menu_service.dto.request.CreateMenuItemRequest;
import com.debjitpal.menu_service.dto.response.ApiResponse;
import com.debjitpal.menu_service.dto.response.MenuItemResponse;
import com.debjitpal.menu_service.service.impl.MenuItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemServiceImpl menuItemService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> addMenuItem
            (@RequestBody CreateMenuItemRequest menuItemRequest){

        MenuItemResponse response = menuItemService.createMenuItem(menuItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                .success("Menu item added successfully.", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItemById(@PathVariable UUID id) {

        MenuItemResponse response = menuItemService.getMenuItemById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse
                .success("Menu item fetched successfully. ", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAllMenuItem() {

        List<MenuItemResponse> responses = menuItemService.getAllMenuItem();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse
                        .success("Menu item fetched successfully.", responses)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem
            (@PathVariable UUID id, @RequestBody CreateMenuItemRequest menuItemRequest) {

        MenuItemResponse response = menuItemService.updateMenuItem(id, menuItemRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success
                        ("Menu item updated successfully", response)
                );
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updatePrice
            (@PathVariable UUID id, @RequestParam Double price) {

        MenuItemResponse response = menuItemService.updatePrice(id, price);

        return ResponseEntity.ok
                (ApiResponse.success
                        ("Price updated successfully", response)
                );
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<MenuItemResponse>> adjustAvailability
            (@PathVariable UUID id, @RequestParam Boolean isAvailable) {

        MenuItemResponse response = menuItemService.enableDisableMenuItem(id, isAvailable);

        return ResponseEntity.ok
                (ApiResponse.success
                        ("Availability updated successfully", response)
                );
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getMenuByRestaurantId(@PathVariable UUID restaurantId) {
        List<MenuItemResponse> responses = menuItemService.getMenuByRestaurantId(restaurantId);

        return ResponseEntity.ok
                (ApiResponse.success
                        ("Menu fetched successfully.", responses)
                );
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable UUID id) {
//        menuItemService.deleteMenuItem(id);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse
//                .success("Menu item deleted successfully", null)
//        );
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable UUID id) {
        menuItemService.deleteMenuItem(id);

        return ResponseEntity.ok(
                ApiResponse.success("Menu item deleted successfully", null)
        );
    }
}

package com.debjitpal.menu_service.service.impl;

import com.debjitpal.menu_service.dto.request.CreateMenuItemRequest;
import com.debjitpal.menu_service.dto.response.ApiResponse;
import com.debjitpal.menu_service.dto.response.MenuItemResponse;
import com.debjitpal.menu_service.dto.response.RestaurantResponse;
import com.debjitpal.menu_service.entity.MenuItem;
import com.debjitpal.menu_service.exception.MenuItemNotFoundException;
import com.debjitpal.menu_service.external.service.RestaurantFeignClient;
import com.debjitpal.menu_service.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantFeignClient restaurantFeignClient;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private MenuItem menuItem;
    private UUID menuItemId;
    UUID restaurantId = UUID.randomUUID();
    private CreateMenuItemRequest createMenuItemRequest;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        menuItem = MenuItem.builder()
                .id(menuItemId)
                .restaurantId(restaurantId)
                .name("Biriyani")
                .description("Taste these delectable classics, delectable biryanis to make your day.")
                .price(360.00)
                .isAvailable(true)
                .build();

        createMenuItemRequest = new CreateMenuItemRequest(
                restaurantId,
                "Biriyani",
                "Taste these delectable classics, delectable biryanis to make your day.",
                360.00,
                true
        );
    }

    @Test
    void createMenuItem() {
        given(menuItemRepository.save(any(MenuItem.class))).willReturn(menuItem);
        MenuItemResponse response = menuItemService.createMenuItem(createMenuItemRequest);

        assertThat(response).isNotNull();
        assertEquals("Biriyani", response.getName());
        assertEquals(360.00, response.getPrice());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_success() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.of(menuItem));
        given(menuItemRepository.save(any(MenuItem.class))).willReturn(menuItem);

        MenuItemResponse response = menuItemService.updateMenuItem(menuItemId, createMenuItemRequest);

        assertThat(response).isNotNull();
        assertEquals("Biriyani", response.getName());
        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void updateMenuItem_notFound() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class,
                () -> menuItemService.updateMenuItem(menuItemId, createMenuItemRequest));

        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void getAllMenuItem() {
        given(menuItemRepository.findAll()).willReturn(List.of(menuItem));

        List<MenuItemResponse> responses = menuItemService.getAllMenuItem();

        assertThat(responses).isNotEmpty();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("Biriyani");

        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    void getMenuItemById_whenIdExits() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.of(menuItem));
        MenuItemResponse response = menuItemService.getMenuItemById(menuItemId);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Biriyani");
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void getMenuItemById_whenIdNotExits() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class,
                () -> menuItemService.getMenuItemById(menuItemId));
    }

    @Test
    void updatePrice_success() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.of(menuItem));
        given(menuItemRepository.save(any(MenuItem.class))).willReturn(menuItem);

        MenuItemResponse response = menuItemService.updateMenuItem(menuItemId, createMenuItemRequest);

        assertThat(response).isNotNull();
        assertEquals(360.00, response.getPrice());
        assertEquals("Biriyani", response.getName());

        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void updatePrice_itemNotFound() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class,
                () -> menuItemService.getMenuItemById(menuItemId));

        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void enableDisableMenuItem_success() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.of(menuItem));
        given(menuItemRepository.save(any(MenuItem.class))).willReturn(menuItem);

        MenuItemResponse response = menuItemService.enableDisableMenuItem(menuItemId, true);

        assertThat(response).isNotNull();
        assertThat(response.getIsAvailable()).isTrue();

        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void enableDisableMenuItem_itemNotFound() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class,
                () -> menuItemService.enableDisableMenuItem(menuItemId, false));

        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void getMenuByRestaurantId() {
        RestaurantResponse restaurantResponse = RestaurantResponse.builder()
                .id(restaurantId)
                .name("The RestaurantHouse")
                .isOpen(true)
                .build();

        ApiResponse<RestaurantResponse> apiResponse =
                ApiResponse.success("Restaurant fetched", restaurantResponse);

        given(restaurantFeignClient.getRestaurantById(restaurantId))
                .willReturn(apiResponse);

        given(menuItemRepository.findByRestaurantId(restaurantId))
                .willReturn(List.of(menuItem));

        // when
        List<MenuItemResponse> result =
                menuItemService.getMenuByRestaurantId(restaurantId);

        // then
        assertEquals(1, result.size());
        verify(menuItemRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void deleteMenuItem_success() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.of(menuItem));

        doNothing().when(menuItemRepository).delete(menuItem);

        menuItemService.deleteMenuItem(menuItemId);

        verify(menuItemRepository, times(1)).findById(menuItemId);
        verify(menuItemRepository, times(1)).delete(menuItem);
    }

    @Test
    void deleteMenuItem_notFound() {
        given(menuItemRepository.findById(menuItemId)).willReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.deleteMenuItem(menuItemId);
        });

        verify(menuItemRepository, never()).delete(any(MenuItem.class));
    }
}
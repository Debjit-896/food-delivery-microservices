package com.debjitpal.restaurant_service.service.impl;

import com.debjitpal.restaurant_service.dto.request.CreateRestaurantRequest;
import com.debjitpal.restaurant_service.dto.response.RestaurantResponse;
import com.debjitpal.restaurant_service.exception.RestaurantNotFoundException;
import com.debjitpal.restaurant_service.model.Restaurant;
import com.debjitpal.restaurant_service.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private UUID restaurantId;
    private CreateRestaurantRequest restaurantRequest;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("ITC Royal Bengal")
                .address("1, JBS Haldane Avenue, Tangra, Kolkata, West Bengal - 700046, India")
                .rating(4.5)
                .city("Kolkata")
                .isOpen(true)
                .build();

        restaurantRequest = new CreateRestaurantRequest(
                "ITC Royal Bengal",
                "1, JBS Haldane Avenue, Tangra, Kolkata, West Bengal - 700046, India",
                "Kolkata",
                4.5,
                true
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createRestaurant() {
        when(restaurantRepository.save(any(Restaurant.class)))
                .thenReturn(restaurant);

        RestaurantResponse response = restaurantService.createRestaurant(restaurantRequest);
        assertEquals("ITC Royal Bengal", response.getName());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void updateRestaurant_success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, restaurantRequest);

        assertEquals("ITC Royal Bengal", response.getName());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void updateRestaurant_notFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.updateRestaurant(restaurantId, restaurantRequest));

    }

    @Test
    void getAllRestaurant_success() {
        when(restaurantRepository.findAll())
                .thenReturn(List.of(restaurant));

        List<RestaurantResponse> responses =
                restaurantService.getAllRestaurant();

        assertAll(
                ()-> assertEquals(1, responses.size()),
                ()-> assertEquals("ITC Royal Bengal", responses.getFirst().getName()),
                ()-> assertEquals("Kolkata", responses.getFirst().getCity())
        );
    }

    @Test
    void getAllRestaurant_notFound() {
        when(restaurantRepository.findAll()).thenReturn(List.of());

        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.getAllRestaurant());
    }

    @Test
    void getRestaurantById_success() {
        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        RestaurantResponse response =
                restaurantService.getRestaurantById(restaurantId);

        assertEquals("ITC Royal Bengal", response.getName());
    }

    @Test
    void getRestaurantById_success_notFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.getRestaurantById(restaurantId));
    }

    @Test
    void findByRating_success() {
        when(restaurantRepository.findByRating(4.5)).thenReturn(List.of(restaurant));

        List<RestaurantResponse> responses = restaurantService.findByRating(4.5);

        assertAll(
                ()-> assertEquals("ITC Royal Bengal", responses.getFirst().getName()),
                ()-> assertEquals(4.5, responses.getFirst().getRating())
        );

    }

    @Test
    void findByRating_notFound() {
        when(restaurantRepository.findByRating(4.5))
                .thenReturn(List.of());

        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.findByRating(4.5));
    }

    @Test
    void findByCity_success() {
        when(restaurantRepository.findByCity("Kolkata")).thenReturn(List.of(restaurant));

        List<RestaurantResponse> responses = restaurantService.findByCity("Kolkata");

        assertAll(
                ()-> assertEquals("ITC Royal Bengal", responses.getFirst().getName()),
                ()-> assertEquals("Kolkata", responses.getFirst().getCity())
        );
    }

    @Test
    void findByCity_notFound() {
        when(restaurantRepository.findByCity("Mumbai"))
                .thenReturn(List.of());

        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.findByCity("Mumbai"));
    }

    @Test
    void deleteRestaurant_success() {
        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurant(restaurantId);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void deleteRestaurant_notFound() {
        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.deleteRestaurant(restaurantId));
    }
}
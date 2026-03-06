package com.debjitpal.restaurant_service.service;

import com.debjitpal.restaurant_service.dto.request.CreateRestaurantRequest;
import com.debjitpal.restaurant_service.dto.response.RestaurantResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {

    RestaurantResponse createRestaurant(CreateRestaurantRequest restaurantRequest);
    RestaurantResponse updateRestaurant(UUID id, CreateRestaurantRequest restaurantRequest);
    List<RestaurantResponse> getAllRestaurant();
    RestaurantResponse getRestaurantById(UUID id);
    List<RestaurantResponse> findByRating(Double rating);
    List<RestaurantResponse> findByCity(String city);
    void deleteRestaurant(UUID id);
}

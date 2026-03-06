package com.debjitpal.restaurant_service.service.impl;

import com.debjitpal.restaurant_service.dto.request.CreateRestaurantRequest;
import com.debjitpal.restaurant_service.dto.response.RestaurantResponse;
import com.debjitpal.restaurant_service.exception.RestaurantNotFoundException;
import com.debjitpal.restaurant_service.model.Restaurant;
import com.debjitpal.restaurant_service.repository.RestaurantRepository;
import com.debjitpal.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest restaurantRequest) {
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantRequest.getName())
                .address(restaurantRequest.getAddress())
                .city(restaurantRequest.getCity())
                .rating(restaurantRequest.getRating())
                .isOpen(restaurantRequest.getIsOpen())
                .build();
        return mapToResponse(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantResponse updateRestaurant(UUID id, CreateRestaurantRequest restaurantRequest) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(()-> new RestaurantNotFoundException
                        ("Restaurant not found with id " +id)
                );

        restaurant.setName(restaurantRequest.getName());
        restaurant.setAddress(restaurantRequest.getAddress());
        restaurant.setCity(restaurantRequest.getCity());
        restaurant.setRating(restaurantRequest.getRating());
        restaurant.setIsOpen(restaurantRequest.getIsOpen());

        return mapToResponse(restaurantRepository.save(restaurant));
    }

    @Override
    public List<RestaurantResponse> getAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        if (restaurants.isEmpty()){
            throw new RestaurantNotFoundException("No restaurants available");
        }

        return restaurants.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RestaurantResponse getRestaurantById(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException
                        ("Restaurant not found with id " +id)
                );

        return mapToResponse(restaurant);
    }

    @Override
    public List<RestaurantResponse> findByRating(Double rating) {
        List<Restaurant> restaurants = restaurantRepository.findByRating(rating);

        if (restaurants.isEmpty()){
            throw new RestaurantNotFoundException("No restaurants found with rating " +rating);
        }

        return restaurants.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponse> findByCity(String city) {
        List<Restaurant> restaurants = restaurantRepository.findByCity(city);

        if (restaurants.isEmpty()){
            throw new RestaurantNotFoundException("No restaurants found in city: " +city);
        }

        return restaurants.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteRestaurant(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException
                        ("Restaurant not found with id " +id)
                );
        restaurantRepository.delete(restaurant);
        log.warn("Restaurant deleted - Id: {} Name: {}",id, restaurant.getName());
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .rating(restaurant.getRating())
                .isOpen(restaurant.getIsOpen())
                .build();
    }
}

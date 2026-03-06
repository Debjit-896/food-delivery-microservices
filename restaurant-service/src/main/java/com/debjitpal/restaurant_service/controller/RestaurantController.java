package com.debjitpal.restaurant_service.controller;


import com.debjitpal.restaurant_service.dto.request.CreateRestaurantRequest;
import com.debjitpal.restaurant_service.dto.response.ApiResponse;
import com.debjitpal.restaurant_service.dto.response.RestaurantResponse;
import com.debjitpal.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant
            (@RequestBody CreateRestaurantRequest request){

        RestaurantResponse response = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                        .success("Restaurant created successfully.", response)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant
            (@PathVariable UUID id, @RequestBody CreateRestaurantRequest request){

        RestaurantResponse response = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                        .success("Restaurant updated successfully.", response)
                );

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurant(){

        List<RestaurantResponse> responses = restaurantService.getAllRestaurant();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse
                        .success("Restaurants fetched successfully.", responses)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurantById(@PathVariable UUID id){

        RestaurantResponse response = restaurantService.getRestaurantById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse
                        .success("Restaurant fetched successfully.", response)
                );
    }

    @GetMapping("/rating/{rating}")
    public  ResponseEntity<ApiResponse<List<RestaurantResponse>>> findByRating(@PathVariable Double rating){

        List<RestaurantResponse> responses = restaurantService.findByRating(rating);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse
                        .success("Restaurants fetched by rating.", responses)
                );
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> findByCity(@PathVariable String city){

        List<RestaurantResponse> responses = restaurantService.findByCity(city);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse
                        .success("Restaurants fetched by city.", responses)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable UUID id){
        restaurantService.deleteRestaurant(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse
                        .success("Restaurant deleted successfully.", null)
                );
    }
}

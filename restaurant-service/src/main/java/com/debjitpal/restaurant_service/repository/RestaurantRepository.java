package com.debjitpal.restaurant_service.repository;

import com.debjitpal.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    List<Restaurant> findByRating(Double rating);
    List<Restaurant> findByCity(String city);
}

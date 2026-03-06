package com.debjitpal.restaurant_service.repository;

import com.debjitpal.restaurant_service.model.Restaurant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;


    private Restaurant restaurant;


    @BeforeEach
    void setUp() {
        // insert row in restaurant table
        restaurant = Restaurant.builder()
                .name("ITC Royal Bengal")
                .address("1, JBS Haldane Avenue, Tangra, Kolkata, West Bengal - 700046, India")
                .city("Kolkata")
                .rating(4.5)
                .isOpen(true)
                .build();

        restaurantRepository.save(restaurant);
    }

    @AfterEach
    void tearDown() {
        // delete row from restaurant table
        restaurantRepository.delete(restaurant);
    }

    @Test
    void findByRating() {
        List<Restaurant> restaurants = restaurantRepository.findByRating(4.5);
        assertNotNull(restaurants);
        assertFalse(restaurants.isEmpty());
        assertEquals("ITC Royal Bengal", restaurants.getFirst().getName());
    }

    @Test
    void findByCity() {
        List<Restaurant> restaurants = restaurantRepository.findByCity("Kolkata");
        assertNotNull(restaurants);
        assertEquals(1,restaurants.size());
        assertEquals("Kolkata", restaurants.getFirst().getCity());
    }
}
package com.debjitpal.menu_service.repository;

import com.debjitpal.menu_service.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID>  {
    List<MenuItem> findByRestaurantId(UUID restaurantId);
}

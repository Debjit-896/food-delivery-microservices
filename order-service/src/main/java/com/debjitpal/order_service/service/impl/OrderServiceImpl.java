package com.debjitpal.order_service.service.impl;

import com.debjitpal.order_service.dto.request.OrderItemRequestDto;
import com.debjitpal.order_service.dto.request.OrderRequestDto;
import com.debjitpal.order_service.dto.response.*;
import com.debjitpal.order_service.entity.Order;
import com.debjitpal.order_service.entity.OrderItem;
import com.debjitpal.order_service.entity.OrderStatus;
import com.debjitpal.order_service.entity.OrderStatusHistory;
import com.debjitpal.order_service.exception.ResourceNotFoundException;
import com.debjitpal.order_service.external.service.MenuClient;
import com.debjitpal.order_service.external.service.RestaurantClient;
import com.debjitpal.order_service.repository.OrderRepository;
import com.debjitpal.order_service.repository.OrderStatusHistoryRepository;
import com.debjitpal.order_service.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;
    private final MenuClient menuClient;
    private final OrderStatusHistoryRepository orderHistoryRepository;


    @Override
    public OrderResponseDto placeOrder(OrderRequestDto request) {

        // Validate restaurant
        ApiResponse<RestaurantResponse> restaurantResponse =
                restaurantClient.getRestaurantById(request.getRestaurantId());

        if (restaurantResponse.getData() == null) {
            throw new ResourceNotFoundException("Restaurant not found!");
        }

        Order order = new Order();

        order.setCustomerId(request.getCustomerId());
        order.setRestaurantId(request.getRestaurantId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryAddress(request.getDeliveryAddress());

        List<OrderItem> items = new ArrayList<>();

        double total = 0;

        for (OrderItemRequestDto itemRequest : request.getItems()) {

            // Fetch menu item from Menu Service
            ApiResponse<MenuItemResponse> menuResponse =
                    menuClient.getMenuItemById(itemRequest.getMenuItemId());

            MenuItemResponse menuItem = menuResponse.getData();

            if (menuItem == null) {
                throw new ResourceNotFoundException("Menu item not found with id: " + itemRequest.getMenuItemId());
            }

            if (!menuItem.getIsAvailable()) {
                throw new ResourceNotFoundException("Menu item is currently unavailable.");
            }

            OrderItem item = new OrderItem();

            item.setMenuItemId(menuItem.getId());
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(menuItem.getPrice()); // price from menu service
            item.setSpecialInstructions(itemRequest.getSpecialInstructions());
            item.setOrder(order);

            total += menuItem.getPrice() * itemRequest.getQuantity();

            items.add(item);
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public OnlyOrderResponse updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oder Not Found With ID: " + orderId)
                );

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is cancelled and cannot be changed.");
        }

        order.setStatus(newStatus);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(orderId)
                .status(newStatus)
                .updatedAt(LocalDateTime.now())
                .build();

        orderHistoryRepository.save(history);

        return mapToOnlyResponse(order);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oder Not Found With ID: " + orderId)
                );

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    @Override
    public List<OnlyOrderResponse> getCustomerOrders(UUID customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToOnlyResponse)
                .toList();
    }

    @Override
    public OnlyOrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order Not Found With ID: " + orderId)
                );

        return mapToOnlyResponse(order);
    }

    @Override
    public List<OrderStatusHistoryResponse> orderHistory(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId)
                );

        List<OrderStatusHistory> historyList = orderHistoryRepository.findByOrderId(orderId);

        if (historyList.isEmpty()) {
            throw new ResourceNotFoundException("No order history found for order id: " + orderId);
        }

        return historyList.stream()
                .map(history -> OrderStatusHistoryResponse.builder()
                        .status(history.getStatus().name())
                        .updatedAt(history.getUpdatedAt())
                        .build()
                )
                .toList();
    }

    private OrderResponseDto mapToResponse(Order order){

        List<OrderItemResponse> responses = order.getOrderItems()
                .stream()
                .map(orderItem -> OrderItemResponse.builder()
                        .id(orderItem.getId())
                        .menuItemId(orderItem.getMenuItemId())
                        .quantity(orderItem.getQuantity())
                        .price(orderItem.getPrice())
                        .specialInstructions(orderItem.getSpecialInstructions())
                        .build()
                )
                .toList();

        return OrderResponseDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .deliveryAddress(order.getDeliveryAddress())
                .items(responses)
                .build();
    }

    private OnlyOrderResponse mapToOnlyResponse(Order order) {
        return OnlyOrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
    }

}

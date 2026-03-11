package com.debjitpal.order_service.service.impl;

import com.debjitpal.order_service.dto.request.OrderItemRequestDto;
import com.debjitpal.order_service.dto.request.OrderRequestDto;
import com.debjitpal.order_service.dto.response.*;
import com.debjitpal.order_service.entity.Order;
import com.debjitpal.order_service.entity.OrderStatus;
import com.debjitpal.order_service.exception.ResourceNotFoundException;
import com.debjitpal.order_service.external.service.MenuClient;
import com.debjitpal.order_service.external.service.RestaurantClient;
import com.debjitpal.order_service.repository.OrderRepository;
import com.debjitpal.order_service.repository.OrderStatusHistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantClient restaurantClient;

    @Mock
    private MenuClient menuClient;

    @Mock
    private OrderStatusHistoryRepository historyRepository;

    private Order order;
    private OrderRequestDto orderRequestDto;
    private OrderItemRequestDto itemRequestDto;

    private UUID orderId;
    private UUID restaurantId;
    private UUID customerId;
    private UUID menuItemId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        menuItemId = UUID.randomUUID();

        order = new Order();
        order.setId(orderId);
        order.setRestaurantId(restaurantId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.CREATED);

        itemRequestDto = new OrderItemRequestDto();
        itemRequestDto.setMenuItemId(menuItemId);
        itemRequestDto.setQuantity(2);

        orderRequestDto = OrderRequestDto.builder()
                .customerId(customerId)
                .restaurantId(restaurantId)
                .deliveryAddress("18 Rabindra Sarabore lake kolkata, 700012, WB")
                .items(List.of(itemRequestDto))
                .build();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void placeOrder_success() {

        RestaurantResponse restaurant = new RestaurantResponse();
        ApiResponse<RestaurantResponse> restaurantApi =
                ApiResponse.success("Restaurant found", restaurant);

        MenuItemResponse menuItem = new MenuItemResponse();
        menuItem.setId(menuItemId);
        menuItem.setPrice(200.0);
        menuItem.setIsAvailable(true);

        ApiResponse<MenuItemResponse> menuApi =
                ApiResponse.success("Menu item found", menuItem);

        given(restaurantClient.getRestaurantById(restaurantId))
                .willReturn(restaurantApi);

        given(menuClient.getMenuItemById(menuItemId))
                .willReturn(menuApi);

        given(orderRepository.save(any(Order.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto response = orderService.placeOrder(orderRequestDto);

        assertThat(response).isNotNull();
        assertEquals(customerId, response.getCustomerId());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_restaurantNotFound() {

        ApiResponse<RestaurantResponse> response =
                ApiResponse.success("Restaurant missing", null);

        given(restaurantClient.getRestaurantById(restaurantId)).willReturn(response);

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrder(orderRequestDto));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder() {
    }

    @Test
    void updateOrderStatus() {
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        OnlyOrderResponse response = orderService.updateOrderStatus(orderId, "DISPATCHED");

        assertThat(response).isNotNull();
        assertEquals("DISPATCHED", response.getStatus());

        verify(historyRepository).save(any());
    }

    @Test
    void updateOrderStatus_notFound() {
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                ()-> orderService.updateOrderStatus(orderId, "DISPATCHED"));
    }

    @Test
    void cancelOrder_success() {
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        orderService.cancelOrder(orderId);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void cancelOrder_orderNotFound() {
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> orderService.cancelOrder(orderId));
    }

    @Test
    void getCustomerOrders_success() {
        given(orderRepository.findByCustomerId(customerId)).willReturn(List.of(order));

        List<OnlyOrderResponse> responses = orderService.getCustomerOrders(customerId);

        assertThat(responses).isNotEmpty();
        assertEquals(1, responses.size());

        verify(orderRepository).findByCustomerId(customerId);
    }

    @Test
    void getCustomerOrders_orderNotFound() {

        given(orderRepository.findByCustomerId(customerId)).willReturn(Collections.emptyList());

        List<OnlyOrderResponse> responses = orderService.getCustomerOrders(customerId);

        assertThat(responses).isEmpty();

        verify(orderRepository).findByCustomerId(customerId);
    }

    // implement using customer client
//    @Test
//    void getCustomerOrders_customerNotFound() {
//
//    }

    @Test
    void getOrderById_success() {
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        OnlyOrderResponse response = orderService.getOrderById(orderId);

        assertThat(response).isNotNull();
        assertEquals(orderId, response.getId());
    }

    @Test
    void getOrderById_orderNotFound() {
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void orderHistory_success() {
        given(orderRepository.findByCustomerId(customerId)).willReturn(List.of(order));

        List<OnlyOrderResponse> responses = orderService.getCustomerOrders(customerId);

        assertThat(responses).isNotEmpty();
        assertEquals(1, responses.size());

        verify(orderRepository).findByCustomerId(customerId);
    }

    @Test
    void orderHistory_emptyList() {
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(historyRepository.findByOrderId(orderId)).willReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> orderService.orderHistory(orderId));
    }
}
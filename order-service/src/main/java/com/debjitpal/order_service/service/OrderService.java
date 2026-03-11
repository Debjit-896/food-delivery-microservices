package com.debjitpal.order_service.service;

import com.debjitpal.order_service.dto.request.OrderRequestDto;
import com.debjitpal.order_service.dto.response.OnlyOrderResponse;
import com.debjitpal.order_service.dto.response.OrderResponseDto;
import com.debjitpal.order_service.dto.response.OrderStatusHistoryResponse;
import com.debjitpal.order_service.entity.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto request);
    OnlyOrderResponse updateOrderStatus(UUID orderId, String status);
    void cancelOrder(UUID orderId);
    List<OnlyOrderResponse> getCustomerOrders(UUID customerId);
    OnlyOrderResponse getOrderById(UUID orderId);
    List<OrderStatusHistoryResponse> orderHistory(UUID orderId);
}

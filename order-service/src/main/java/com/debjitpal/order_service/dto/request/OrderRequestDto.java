package com.debjitpal.order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private UUID customerId;
    private UUID restaurantId;
    private String deliveryAddress;
    private List<OrderItemRequestDto> items;
}

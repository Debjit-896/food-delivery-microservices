package com.debjitpal.order_service.dto.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {
    private UUID customerId;
    private UUID restaurantId;
    private String deliveryAddress;
    private List<OrderItemRequestDto> items;
}

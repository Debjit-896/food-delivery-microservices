package com.debjitpal.order_service.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDto {
    private UUID menuItemId;
    private Integer quantity;
    private String specialInstructions;
}

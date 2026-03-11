package com.debjitpal.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private UUID id;
    private UUID menuItemId;
    private Integer quantity;
    private Double price;
    private String specialInstructions;
}

package com.debjitpal.order_service.dto.response;

import com.debjitpal.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusHistoryResponse {
    private UUID orderId;
    private String status;
    private LocalDateTime updatedAt;
}

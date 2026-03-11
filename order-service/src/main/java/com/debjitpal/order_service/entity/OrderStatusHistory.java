package com.debjitpal.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime updatedAt;
}

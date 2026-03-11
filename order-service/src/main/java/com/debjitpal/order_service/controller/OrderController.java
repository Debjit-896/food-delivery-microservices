package com.debjitpal.order_service.controller;

import com.debjitpal.order_service.dto.request.OrderRequestDto;
import com.debjitpal.order_service.dto.request.UpdateOrderStatusRequest;
import com.debjitpal.order_service.dto.response.ApiResponse;
import com.debjitpal.order_service.dto.response.OnlyOrderResponse;
import com.debjitpal.order_service.dto.response.OrderResponseDto;
import com.debjitpal.order_service.entity.OrderStatusHistory;
import com.debjitpal.order_service.repository.OrderStatusHistoryRepository;
import com.debjitpal.order_service.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> placeOrder
            (@RequestBody OrderRequestDto requestDto){

        OrderResponseDto responseDto = orderService.placeOrder(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Order successfully placed.",responseDto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OnlyOrderResponse>> updateStatus
            (@PathVariable UUID id, @RequestBody UpdateOrderStatusRequest statusRequest){

        OnlyOrderResponse responseDto = orderService.updateOrderStatus(id, statusRequest.getStatus());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Status Updated Successful.", responseDto));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable UUID id) {
        orderService.cancelOrder(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Order canceled successful.", null));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OnlyOrderResponse>>> getCustomerOrders
            (@PathVariable UUID customerId){

        List<OnlyOrderResponse> responseDto = orderService.getCustomerOrders(customerId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Customers order fetched successfully.", responseDto)
                );
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<OrderStatusHistory>>> getOrderHistory(@PathVariable UUID id) {

        List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Order history fetched successfully", history));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OnlyOrderResponse>> getOrderById(@PathVariable UUID id){
        OnlyOrderResponse responseDto = orderService.getOrderById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Order fetched successfully", responseDto));
    }
}

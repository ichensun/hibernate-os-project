package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.dto.OrderDTO;
import com.bfs.hibernateprojectdemo.dto.PlaceOrderRequest;
import com.bfs.hibernateprojectdemo.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderDetail(@PathVariable Long id) {
        return orderService.getOrderDetail(id);
    }

    @PostMapping
    public ResponseEntity<String> placeNewOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        orderService.placeNewOrder(placeOrderRequest);

        return ResponseEntity.ok(placeOrderRequest.getUserId() + " place" +
                " order successfully");
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id) {
        orderService.updateOrderStatus(id);

        return ResponseEntity.ok(id + " updated");
    }
}

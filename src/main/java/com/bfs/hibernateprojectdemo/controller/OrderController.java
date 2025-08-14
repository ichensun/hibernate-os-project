package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.dto.OrderDTO;
import com.bfs.hibernateprojectdemo.dto.PlaceOrderRequest;
import com.bfs.hibernateprojectdemo.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    // admin
    @PreAuthorize("hasRole(1)")
    @GetMapping("/all")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    // user
    @PreAuthorize("hasRole(0)")
    @GetMapping("/{id}")
    public OrderDTO getOrderDetail(@PathVariable Long id) {
        return orderService.getOrderDetail(id);
    }

    // user
    @PreAuthorize("hasRole(0)")
    @PostMapping
    public ResponseEntity<String> placeNewOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        orderService.placeNewOrder(placeOrderRequest);

        return ResponseEntity.ok(placeOrderRequest.getUserId() + " place" +
                " order successfully");
    }

    // user
    @PreAuthorize("hasRole(0)")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id) {
        orderService.updateOrderStatus(id);

        return ResponseEntity.ok(id + " updated");
    }

}

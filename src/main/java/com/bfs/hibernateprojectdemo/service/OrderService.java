package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.OrderDao;
import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.dto.OrderDTO;
import com.bfs.hibernateprojectdemo.dto.PlaceOrderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class OrderService {

    private OrderDao orderDao;

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }


    @Transactional
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderDao.getAllOrders();
        return orders.stream()
                .map(order -> OrderDTO.builder()
                        .orderId(order.getOrderId())
                        .datePlaced(order.getDatePlaced())
                        .orderStatus(order.getOrderStatus())
                        .userId(order.getUser().getUserId().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO getOrderDetail(Long orderId) {
        Order orderDetail = orderDao.getOrderDetail(orderId);
        return OrderDTO.builder()
                .orderId(orderDetail.getOrderId())
                .datePlaced(orderDetail.getDatePlaced())
                .orderStatus(orderDetail.getOrderStatus())
                .userId(String.valueOf(orderDetail.getUser().getUserId()))
                .build();
    }

    @Transactional
    public void placeNewOrder(PlaceOrderRequest placeOrderRequest) {
        orderDao.placeNewOrder(placeOrderRequest.getOrder(),
                placeOrderRequest.getUserId());
    }

    @Transactional
    public void updateOrderStatus(Long orderId) {
        orderDao.updateOrderStatus(orderId);
    }

}

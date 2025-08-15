package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.ProductOrderRequest;
import com.bfs.hibernateprojectdemo.exception.CancelCompeteOrderException;
import com.bfs.hibernateprojectdemo.exception.NotEnoughInventoryException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class OrderDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Order> getAllOrders() {
        List<Order> orders;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            orders = session.createQuery("FROM Order o JOIN FETCH o.user u",
                            Order.class).getResultList();
            System.out.println(orders);

            tx.commit();
        }
        return orders;
    }

    public Order getOrderDetail(Long orderId) {
        Order order = sessionFactory.getCurrentSession()
                .createQuery("from Order o WHERE o.orderId = :orderId",
                        Order.class)
                .setParameter("orderId", orderId)
                .uniqueResult();

        return order;
    }

    public void placeNewOrder(List<ProductOrderRequest> orderList, Long userId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();

            User user = session.get(User.class, userId);
            // 1. Create Order
            Order order = Order.builder()
                    .datePlaced(LocalDateTime.now())
                    .orderStatus("Processing")
                    .user(user)
                    .build();
            // place order's user id should based on current JWT token to
            // determine which user place new orders, but authentication does
            // not work right now so hardcode it with user_id 1);
            session.persist(order);

            // 2. Loop through order items
            for (ProductOrderRequest itemReq : orderList) {
                Long productId = itemReq.getProductId();

                if (productId == null) {
                    throw new IllegalArgumentException("Product ID is missing in order item: " + itemReq);
                }

                Product product = session.get(Product.class, productId);
                if (product == null) {
                    throw new EntityNotFoundException("Product with ID " + productId + " not found");
                }

                // Increment stock
                product.setQuantity(product.getQuantity() - itemReq.getQuantity());
                session.update(product);

                // Create order item
                OrderItem orderItem = OrderItem.builder()
                        .purchasedPrice(product.getRetailPrice())
                        .quantity(itemReq.getQuantity())
                        .wholesalePrice(product.getWholesalePrice())
                        .order(order)
                        .product(product)
                        .build();

                session.persist(orderItem);
            }
            tx.commit();
        } catch (NotEnoughInventoryException e) {
            tx.rollback();
            throw e;
        }
    }

    public void updateOrderStatus(Long orderId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Order order = session.get(Order.class, orderId);
            if (order == null) {
                throw new EntityNotFoundException("Order not found with ID: " + orderId);
            }

            if ("Completed".equalsIgnoreCase(order.getOrderStatus())) {
                throw new CancelCompeteOrderException("Completed orders cannot be canceled.");
            }

            if (!"Processing".equalsIgnoreCase(order.getOrderStatus())) {
                throw new IllegalStateException("Only orders in 'Processing' state can be canceled.");
            }

            // 1. Update order status
            order.setOrderStatus("Canceled");
            session.update(order);

            // 2. Reverse stock & delete order items
            List<OrderItem> items = new ArrayList<>(order.getOrderItems());
            for (OrderItem item : items) {
                // Remove from parent list to break the link
                order.getOrderItems().remove(item);

                // Update stock
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                session.update(product);

                // delete order_item row/record
                session.delete(item);
            }

            tx.commit();
        }
    }
}

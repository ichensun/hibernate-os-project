package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.StatsDTO;
import com.bfs.hibernateprojectdemo.dto.UpdateProductRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@Repository
public class ProductDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public ProductDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Product> getAllInStockProducts() {
        List<Product> products;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            products = session.createQuery("FROM Product p WHERE p.quantity >" +
                    " 0", Product.class).getResultList();
            tx.commit();
        }
        return products;
    }

    public Product getProductDetailById(Long productId) {
        return sessionFactory
                .getCurrentSession()
                .get(Product.class, productId);
    }

    public List<Product> getWatchlistProducts(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("SELECT u FROM users u JOIN FETCH" +
                            " u.watchlist WHERE u.userId = :userId",
                    User.class).setParameter("userId", userId).uniqueResult();

            if (user == null) {
                throw new EntityNotFoundException("User not found with ID: " + userId);
            }
            return user.getWatchlist();
        }
    }

    public List<StatsDTO> findTopPurchasedProductsByUser(Long userId, int limit) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT new com.bfs.hibernateprojectdemo.dto.StatsDTO(p, SUM(oi.quantity)) " +
                                    "FROM Order o " +
                                    "JOIN o.orderItems oi " +
                                    "JOIN oi.product p " +
                                    "WHERE o.user.userId = :userId AND o.orderStatus <> 'Canceled' " +
                                    "GROUP BY p.productId " +
                                    "ORDER BY SUM(oi.quantity) DESC, MIN(oi.itemId) ASC", StatsDTO.class)
                    .setParameter("userId", userId)
                    .setMaxResults(limit)
                    .getResultList();
        }
    }

    public void saveProduct(Product product) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(product);
            tx.commit();
        }
    }

    public void updateProduct(Long productId, UpdateProductRequest request) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Product product = session.get(Product.class, productId);
            if (product == null) {
                throw new EntityNotFoundException("Product not found with ID: " + productId);
            }

            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setWholesalePrice(request.getWholesalePrice());
            product.setRetailPrice(request.getRetailPrice());
            product.setQuantity(request.getQuantity());

            session.update(product);
            tx.commit();
        }
    }

}

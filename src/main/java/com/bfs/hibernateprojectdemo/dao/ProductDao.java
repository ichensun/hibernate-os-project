package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

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
            User user = session.createQuery("SELECT u FROM User u JOIN FETCH" +
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
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StatsDTO> query = cb.createQuery(StatsDTO.class);

            // Root entity
            Root<Order> orderRoot = query.from(Order.class);
            System.out.println(orderRoot.getModel());
            Join<Order, OrderItem> orderItemJoin = orderRoot.join("orderItems");
            Join<OrderItem, Product> productJoin = orderItemJoin.join("product");

            query.groupBy(productJoin.get("productId"));

            Expression<Long> sumQuantity = cb.sum(orderItemJoin.get("quantity"));

            query.select(cb.construct(
                    StatsDTO.class,
                    productJoin,
                    sumQuantity
            ));

            query.where(
                    cb.and(
                            cb.equal(orderRoot.get("user").get("userId"), userId),
                            cb.notEqual(orderRoot.get("orderStatus"), "Canceled")
                    )
            );

            // Order by quantity DESC
            query.orderBy(cb.desc(sumQuantity));

            return session.createQuery(query)
                    .setMaxResults(limit)
                    .getResultList();
        }
    }

    public Long saveProduct(Product product) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(product);
            tx.commit();
            return product.getProductId();
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

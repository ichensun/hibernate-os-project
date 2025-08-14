package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.ProductDTO;
import com.bfs.hibernateprojectdemo.dto.UpdateProductRequest;
import com.bfs.hibernateprojectdemo.service.UserService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

@Repository
public class ProductDao {

    private final SessionFactory sessionFactory;

    private final EntityManager entityManager;

    @Autowired
    public ProductDao(SessionFactory sessionFactory, EntityManager entityManager) {
        this.sessionFactory = sessionFactory;
        this.entityManager = entityManager;
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

//    public List<ProductDTO> getMostFrequentlyPurchasedProducts(Long userId, int limit) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
//
//        // Define root and joins
//        Root<OrderItem> orderItemRoot = query.from(OrderItem.class);
//        Join<OrderItem, Order> orderJoin = orderItemRoot.join("order");
//        Join<OrderItem, Product> productJoin = orderItemRoot.join("product");
//        Join<Order, User> userJoin = orderJoin.join("user");
//
//        // Calculate frequency (sum of quantities) and get product details
//        Expression<Long> frequency = cb.sum(orderItemRoot.get("quantity").as(Long.class));
//        Expression<Double> avgPrice = cb.avg(orderItemRoot.get("purchasedPrice"));
//        Expression<LocalDateTime> lastPurchaseDate = cb.greatest(orderJoin.get("datePlaced"));
//
//        // Select grouped fields with aggregations
//        query.multiselect(
//                productJoin.get("productId"),
//                productJoin.get("name"),
//                productJoin.get("description"),
//                frequency,
//                avgPrice,
//                lastPurchaseDate,
//                cb.literal("Completed")
//        );
//
//        // Add WHERE conditions
//        query.where(
//                cb.and(
//                        cb.equal(userJoin.get("userId"), userId),
//                        cb.notEqual(orderJoin.get("orderStatus"), "Canceled")
//                )
//        );
//
//        // Group by product to calculate frequency
//        query.groupBy(
//                productJoin.get("productId"),
//                productJoin.get("name"),
//                productJoin.get("description")
//        );
//
//        // Order by frequency (descending) - most frequent first
//        // Use product_id as tiebreaker for consistency
//        query.orderBy(
//                cb.desc(frequency),
//                cb.desc(productJoin.get("productId"))
//        );
//
//        // Execute query
//        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
//        typedQuery.setMaxResults(limit);
//
//        List<Object[]> results = typedQuery.getResultList();
//
//        // Manually map Object[] to ProductDTO
//        return results.stream()
//                .map(row -> ProductDTO.builder()
//                        .itemId(null)  // Not applicable for frequency-based results
//                        .productId(String.valueOf(row[0]))
//                        .productName((String) row[1])
//                        .description((String) row[2])
//                        .quantity(((Long) row[3]).intValue())  // Total quantity purchased
//                        .purchasedPrice((Double) row[4])       // Average price
//                        .datePlaced((LocalDateTime) row[5])    // Last purchase date
//                        .orderStatus((String) row[6])
//                        .build())
//                .collect(Collectors.toList());
//    }

    public List<ProductDTO> getMostRecentPurchasedProducts(Long userId, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductDTO> query = cb.createQuery(ProductDTO.class);

        // define root and joins
        Root<OrderItem> orderItemRoot = query.from(OrderItem.class);
        Join<OrderItem, Order> orderJoin = orderItemRoot.join("order");
        Join<OrderItem, Product> productJoin = orderItemRoot.join("product");
        Join<Order, User> userJoin = orderJoin.join("user");

        // Create constructor expression for ProductDTO
        Selection<ProductDTO> selection = cb.construct(ProductDTO.class,
                orderItemRoot.get("itemId"),
                productJoin.get("productId"),
                productJoin.get("name"),
                productJoin.get("description"),
                orderItemRoot.get("quantity"),
                orderItemRoot.get("purchasedPrice"),
                orderJoin.get("datePlaced"),
                orderJoin.get("orderStatus")
        );

        query.select(selection);

        // Add WHERE conditions
        query.where(
                cb.and(
                        cb.equal(userJoin.get("userId"), userId),
                        cb.notEqual(orderJoin.get("orderStatus"), "Canceled")
                )
        );

        // Add ORDER BY conditions
        query.orderBy(
                cb.desc(orderJoin.get("datePlaced")),
                cb.desc(orderItemRoot.get("itemId"))
        );

        // Create and execute query
        TypedQuery<ProductDTO> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit);

        return typedQuery.getResultList();
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

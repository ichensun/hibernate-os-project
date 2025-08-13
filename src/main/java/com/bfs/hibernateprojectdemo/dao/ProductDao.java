package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Product;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {

    private SessionFactory sessionFactory;

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

}

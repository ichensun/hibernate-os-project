package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;

@Repository
public class WatchlistDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addToWatchlist(Long userId, Long productId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            User user = session.get(User.class, userId);
            Product product = session.get(Product.class, productId);

            if (user == null || product == null) {
                throw new EntityNotFoundException("User or Product not found");
            }

            // Add product to user's watchlist
            if (!user.getWatchlist().contains(product)) {
                product.getUsers().add(user);
                session.update(product);
            }
            tx.commit();
        }
    }

    public void removeFromWatchlist(Long userId, Long productId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            User user = session.get(User.class, userId);
            Product product = session.get(Product.class, productId);

            if (user == null || product == null) {
                throw new EntityNotFoundException("User or Product not found");
            }

            // Remove product from user's watchlist
            if (user.getWatchlist().contains(product)) {
                product.getUsers().remove(user);
                session.update(product); // this removes from join table
            }

            tx.commit();
        }
    }

}

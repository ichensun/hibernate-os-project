package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SignupDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public SignupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<User> findUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM User u WHERE u.username = :username";
            return session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .setMaxResults(1)
                    .uniqueResultOptional();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public User createUser(String username, String email,
                           String encryptedPassword) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = User.builder()
                .username(username)
                .password(encryptedPassword)
                .role(0)
                .email(email)
                .build();

        session.save(user);
        transaction.commit();

        return user;
    }
}

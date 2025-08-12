package com.bfs.hibernateprojectdemo.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="users")
@Table(name="users")
@Getter
@Setter
@ToString(exclude = {"permissions"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Integer role;

    @Column(name = "username")
    private String username;

    @ManyToMany(mappedBy = "users")
    private List<Product> products;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Permission> permissions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =
            FetchType.LAZY)
    private List<Order> orders;
}

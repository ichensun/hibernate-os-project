package com.bfs.hibernateprojectdemo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "purchased_price")
    private Double purchasedPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "wholesale_price")
    private Double wholesalePrice;

    @ManyToOne
    @JoinColumn(name = "fk_order_item_order", referencedColumnName = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "fk_order_item_product", referencedColumnName =
            "product_id")
    private Product product;
}

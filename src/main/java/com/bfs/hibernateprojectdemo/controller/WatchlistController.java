package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.service.WatchlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private WatchlistService watchlistService;

    @Autowired
    public void setWatchlistService(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/{id}/products/all")
    @PreAuthorize("hasRole(0)")
    public ResponseEntity<List<Product>> getAllWatchlist(@PathVariable Long id) {
        System.out.println(1);
        List<Product> watchlist = watchlistService.getWatchlistProducts(id);
        return ResponseEntity.ok(watchlist);
    }

    @PostMapping("/product/{productId}/user/{userId}")
    public ResponseEntity<String> addProduct(@PathVariable Long userId,
                                             @PathVariable Long productId) {
        watchlistService.addToWatchlist(userId, productId);
        return ResponseEntity.ok("Product added to watchlist");
    }

    @DeleteMapping("/product/{productId}/user/{userId}")
    public ResponseEntity<String> removeProduct(@PathVariable Long userId, @PathVariable Long productId) {
        watchlistService.removeFromWatchlist(userId, productId);
        return ResponseEntity.ok("Product removed from watchlist");
    }

}

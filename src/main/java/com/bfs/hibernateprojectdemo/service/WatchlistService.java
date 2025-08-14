package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.dao.WatchlistDao;
import com.bfs.hibernateprojectdemo.domain.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private ProductDao productDao;
    private WatchlistDao watchlistDao;

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setWatchlistDao(WatchlistDao watchlistDao) {
        this.watchlistDao = watchlistDao;
    }

    public List<Product> getWatchlistProducts(Long userId) {
        return productDao.getWatchlistProducts(userId);
    }

    public void addToWatchlist(Long userId, Long productId) {
        watchlistDao.addToWatchlist(userId, productId);
    }

    public void removeFromWatchlist(Long userId, Long productId) {
        watchlistDao.removeFromWatchlist(userId, productId);
    }
}

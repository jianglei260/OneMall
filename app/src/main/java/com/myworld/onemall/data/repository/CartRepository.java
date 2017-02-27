package com.myworld.onemall.data.repository;

import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.source.CartDataSource;
import com.myworld.onemall.data.source.remote.CartRemoteDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jianglei on 2016/11/30.
 */

public class CartRepository implements CartDataSource {
    private static CartRepository repository;
    private CartRemoteDataSource remote;
    private HashMap<String, Cart> cache;

    public static synchronized CartRepository geRepository() {
        if (repository == null)
            repository = new CartRepository();
        return repository;
    }

    private CartRepository() {
        remote = new CartRemoteDataSource();
        cache = new HashMap<>();
    }

    @Override
    public List<Cart> getCarts(String token) {
        List<Cart> results = remote.getCarts(token);
        saveToCache(results);
        return results;
    }

    @Override
    public List<Cart> getCheckedCarts(String token) {
        List<Cart> results = remote.getCheckedCarts(token);
        resetChecked();
        saveToCache(results);
        return results;
    }

    public void resetChecked() {
        Set<Map.Entry<String, Cart>> entrySet = cache.entrySet();
        for (Map.Entry<String, Cart> stringCartEntry : entrySet) {
            Cart cart = stringCartEntry.getValue();
            cart.setStatus(0);
        }
    }

    public void saveToCache(List<Cart> carts) {
        cache.clear();
        for (Cart cart : carts) {
            cache.put(cart.get_id(), cart);
        }
    }

    @Override
    public boolean check(String token, String cartId, int status) {
        boolean result = remote.check(token, cartId, status);
        if (result) {
            cache.get(cartId).setStatus(status);
        }
        return result;
    }

    @Override
    public boolean add(String token, String goodsId, int num) {
        boolean result = remote.add(token, goodsId, num);
        if (result) {
            getCarts(token);
        }
        return result;
    }

    @Override
    public boolean change(String token, String cartId, int num) {
        boolean result = remote.change(token, cartId, num);
        if (result) {
            Cart cart = cache.get(cartId);
            cart.setGoodsNum(cart.getGoodsNum() + num);
        }
        return result;
    }

    @Override
    public boolean edit(String token, String cartId, int num) {
        boolean result = remote.edit(token, cartId, num);
        ;
        if (result) {
            Cart cart = cache.get(cartId);
            cart.setGoodsNum(num);
        }
        return result;
    }

    public int getGoodsNum() {
        int goodsNum = 0;
        Set<Map.Entry<String, Cart>> entrySet = cache.entrySet();
        for (Map.Entry<String, Cart> stringCartEntry : entrySet) {
            int num = stringCartEntry.getValue().getGoodsNum();
            goodsNum += num;
        }
        return goodsNum;
    }

    public List<Cart> getChecked() {
        List<Cart> checkedCarts = new ArrayList<>();
        Set<Map.Entry<String, Cart>> entrySet = cache.entrySet();
        for (Map.Entry<String, Cart> stringCartEntry : entrySet) {
            Cart cart = stringCartEntry.getValue();
            if (cart.getStatus() == 1) {
                checkedCarts.add(cart);
            }
        }
        return checkedCarts;
    }

    public void clearCache() {
        cache.clear();
    }

    @Override
    public boolean delete(String token, String cartId) {
        boolean result = remote.delete(token, cartId);
        if (result) {
            cache.remove(cartId);
        }
        return result;
    }

    public void removeChecked() {
        Iterator<Map.Entry<String, Cart>> iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            Cart cart = iterator.next().getValue();
            if (cart.getStatus() == 1) {
                iterator.remove();
            }
        }
    }
}

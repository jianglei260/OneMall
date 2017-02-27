package com.myworld.onemall.data.source.remote;

import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.source.CartDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 2016/11/30.
 */

public class CartRemoteDataSource implements CartDataSource
{
    private ApiService service;

    public CartRemoteDataSource() {
        service = GoodsRepository.getRepository().getRemote().getService();
    }

    @Override
    public List<Cart> getCarts(String token) {
        try {
            return service.getCarts(token).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Cart> getCheckedCarts(String token) {
        try {
            return service.getCarts(token,1).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean check(String token, String cartId, int status) {
        try {
            return service.check(token, cartId, status).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(String token, String goodsId, int num) {
        try {
            return service.add(token, goodsId, num).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean change(String token, String cartId, int num) {
        try {
            return service.change(token, cartId, num).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean edit(String token, String cartId, int num) {
        try {
            return service.edit(token, cartId, num).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String token, String cartId) {
        try {
            return service.delete(token, cartId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

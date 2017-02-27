package com.myworld.onemall.data.source;

import com.myworld.onemall.data.entity.Cart;

import java.util.List;

/**
 * Created by jianglei on 2016/11/30.
 */

public interface CartDataSource {
    public List<Cart> getCarts(String token);

    public List<Cart> getCheckedCarts(String token);

    public boolean check(String token, String cartId, int status);

    public boolean add(String token, String goodsId,int num);

    public boolean change(String token, String cartId, int num);

    public boolean edit(String token, String cartId, int num);

    public boolean delete(String token, String cartId);

}

package com.myworld.onemall.data.source;

import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.PayInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jianglei on 2016/12/4.
 */

public interface OrderDataSource {

    public String addOrder(String token, String name, String contact, String address, String message, int type);


    public boolean cancelOrder(String token, String orderid);

    public List<Order> listOrder(String token);


    public Order orderDetail(String token, String orderid);


    public boolean orderReturn(String token, String orderid, String reason);


    public boolean deleteOrder(String token, String orderid);

    public String pay(String token, String oderid);
}

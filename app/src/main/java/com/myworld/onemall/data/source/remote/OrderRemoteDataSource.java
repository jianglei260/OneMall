package com.myworld.onemall.data.source.remote;

import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.PayInfo;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.source.OrderDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 2016/12/4.
 */

public class OrderRemoteDataSource implements OrderDataSource {
    private ApiService service;

    public OrderRemoteDataSource() {
        service = GoodsRepository.getRepository().getRemote().getService();
    }

    @Override
    public String addOrder(String token, String name, String contact, String address, String message, int type) {
        try {
            return service.addOrder(token, name, contact, address, message, type).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean cancelOrder(String token, String orderid) {
        try {
            return service.cancelOrder(token, orderid).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Order> listOrder(String token) {
        try {
            return service.listOrder(token).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Order orderDetail(String token, String orderid) {
        try {
            return service.orderDetail(token, orderid).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean orderReturn(String token, String orderid, String reason) {
        try {
            return service.orderReturn(token, orderid, reason).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteOrder(String token, String orderid) {
        try {
            return service.deleteOrder(token, orderid).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String pay(String token, String oderid) {
        try {
            return service.pay(token, oderid).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

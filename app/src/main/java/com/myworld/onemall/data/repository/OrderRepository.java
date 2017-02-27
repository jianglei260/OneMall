package com.myworld.onemall.data.repository;

import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.PayInfo;
import com.myworld.onemall.data.source.OrderDataSource;
import com.myworld.onemall.data.source.remote.OrderRemoteDataSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jianglei on 2016/12/4.
 */

public class OrderRepository implements OrderDataSource {
    private static OrderRepository repository;
    private OrderRemoteDataSource remote;
    private HashMap<String, Order> cache;
    public static final int ORDER_STATUS_CANCEL = -1;
    public static final int ORDER_STATUS_FINISH = 1;
    public static final int ORDER_STATUS_SENDING = 2;
    public static final int ORDER_STATUS_WILL_SEND = 3;
    public static final int ORDER_STATUS_DELETE = -2;
    public static final int ORDER_STATUS_RECEIVEING = 5;
    public static final int ORDER_STATUS_ALL = 0;
    public static final int ORDER_STATUS_PAYING = -3;

    public static synchronized OrderRepository geRepository() {
        if (repository == null)
            repository = new OrderRepository();
        return repository;
    }

    private OrderRepository() {
        remote = new OrderRemoteDataSource();
        cache = new HashMap<>();
    }

    public void saveToCache(List<Order> orders) {
        for (Order order : orders) {
            cache.put(order.get_id(), order);
        }
    }

    @Override
    public String addOrder(String token, String name, String contact, String address, String message, int type) {
        return remote.addOrder(token, name, contact, address, message, type);
    }

    @Override
    public boolean cancelOrder(String token, String orderid) {
        boolean result = remote.cancelOrder(token, orderid);
        if (result) {
            cache.get(orderid).setStatus(ORDER_STATUS_CANCEL);
        }
        return result;
    }

    public void clearCache() {
        cache.clear();
    }

    @Override
    public List<Order> listOrder(String token) {
        List<Order> results = remote.listOrder(token);
        saveToCache(results);
        return results;
    }

    public List<Order> listOrderByStatus(String token, int status) {
        List<Order> all = listOrder(token);
        if (status == ORDER_STATUS_RECEIVEING)
            return sortByTime(getReceiveing(token));
        if (status == ORDER_STATUS_ALL) {
            return all;
        }
        List<Order> receiveingList = new ArrayList<>();
        for (Order order : all) {
            if (order.getStatus() == status)
                receiveingList.add(order);
        }
        return receiveingList;
    }


    public List<Order> getReceiveing(String token) {
        listOrder(token);
        List<Order> receiveingList = new ArrayList<>();
        Set<Map.Entry<String, Order>> entrySet = cache.entrySet();
        for (Map.Entry<String, Order> stringOrderEntry : entrySet) {
            Order order = stringOrderEntry.getValue();
            switch (order.getStatus()) {
                case ORDER_STATUS_SENDING:
                    receiveingList.add(order);
                    break;
                case ORDER_STATUS_WILL_SEND:
                    receiveingList.add(order);
                    break;
            }
        }
        return receiveingList;
    }

    public List<Order> sortByTime(List<Order> orders) {

        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                SimpleDateFormat format = new SimpleDateFormat();
                try {
                    Date date1 = format.parse(o1.getCreateAt());
                    Date date2 = format.parse(o2.getCreateAt());
                    return -date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return -o1.getCreateAt().compareTo(o2.getCreateAt());
                }
            }
        });
        return orders;
    }

    @Override
    public Order orderDetail(String token, String orderid) {
        if (cache.containsKey(orderid))
            return cache.get(orderid);
        return remote.orderDetail(token, orderid);
    }

    @Override
    public boolean orderReturn(String token, String orderid, String reason) {
        return remote.orderReturn(token, orderid, reason);
    }

    @Override
    public boolean deleteOrder(String token, String orderid) {
        boolean result = remote.deleteOrder(token, orderid);
        if (result)
            cache.remove(orderid);
        return result;
    }

    @Override
    public String pay(String token, String oderid) {
        return remote.pay(token, oderid);
    }
}

package com.myworld.onemall.data.entity;

/**
 * Created by jianglei on 2016/12/4.
 */

public class Return {
    private String _id;
    private Order order;
    private int status;//2.已完成；1.申请中；0.已取消
    private String userId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

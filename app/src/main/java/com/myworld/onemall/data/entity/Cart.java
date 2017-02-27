package com.myworld.onemall.data.entity;

import java.util.List;

/**
 * Created by jianglei on 2016/11/30.
 */

public class Cart {
    /**
     * _id : 583ed2a5a8a6a4330cfdb48a
     * goodsId : 582b13059183ea158c7bfb19
     * userId : 583c4332a519f10e20d2853a
     * status : 0
     * goodsNum : 1
     * createAt : 2016-11-30T13:22:45.974Z
     * updateAt : 2016-11-30T13:27:27.001Z
     * goods : {"_id":"582b13059183ea158c7bfb19","goodsPic":"[{\"url\":\"http://img10.360buyimg.com/n1/jfs/t976/50/582860204/63859/177f2d52/5539e288Nb3e84654.jpg\"},{\"url\":\"http://img10.360buyimg.com/n1/jfs/t1108/356/993436337/68398/58d9d8f0/5563e407N10aa17b2.jpg\"},{\"url\":\"http://img10.360buyimg.com/n1/jfs/t1273/322/649960192/98594/ca729238/5539e2a1N385c594e.jpg\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"},{\"url\":\"\"}]","goodsType":"玩具乐器","goodsName":"大疆 DJI Phantom3/4精灵系列4K/2.7K专业四轴航拍飞行器无人机遥控飞机","goodsPrice":"4999.00","goodsAmount":13,"goodsSize":"精灵3Professional+背包+桨叶","goodsNum":1479217925236,"type":2,"createAt":"2016-11-15T13:52:05.277Z","updateAt":"2016-11-17T09:43:49.319Z","status":2}
     */

    private String _id;
    private String goodsId;
    private String userId;
    private int status;
    private int goodsNum;
    private String createAt;
    private String updateAt;
    private Goods goods;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}

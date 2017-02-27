package com.myworld.onemall.data.entity;

/**
 * Created by jianglei on 2016/11/23.
 */

public class Goods {

    /**
     * _id : 5836e4427e47af08482f5d7c
     * goodsPic : [{"url":"//img12.360buyimg.com/n1/jfs/t3421/201/1338073735/59495/4e069fa3/5823ddabNa028c77a.jpg"},{"url":"//img12.360buyimg.com/n1/jfs/t3814/20/1125195689/98997/f842b1be/5823ddb0Nb7fa39d2.jpg"},{"url":"//img12.360buyimg.com/n1/jfs/t3538/339/1405165474/64117/fdd04e29/5823ddb3N148b4312.jpg"},{"url":"//img12.360buyimg.com/n1/jfs/t3829/262/1116025052/77166/cf21eb00/5823ddb7Nbba4ef41.jpg"},{"url":"//img12.360buyimg.com/n1/jfs/t3673/262/1410703291/83493/6e968d1f/5823ddbcN78000f95.jpg"},{"url":"//img12.360buyimg.com/n1/jfs/t3688/238/1278043298/83806/9ae782c6/5823ddc2Naef70728.jpg"},{"url":"//img12.360buyimg.com/n1/jfs/t3838/282/1099299257/81894/31e22ba6/5823ddc7N2c084bf2.jpg"}]
     * goodsType : 数码
     * goodsName : 小米（MI）无人机1080P含云台机 自动起降 自动返航 自动绕点飞行 设定目的地自动规划航线 三轴机械防抖
     * goodsPrice : 2999
     * goodsAmount : 20
     * goodsSize :
     * goodsNum : 1479992386667
     * status : 1
     * createAt : 2016-11-24T12:59:46.678Z
     * updateAt : 2016-11-24T13:02:23.450Z
     */

    private String _id;
    private String goodsPic;
    private String goodsType;
    private String goodsName;
    private String goodsPrice;
    private int goodsAmount;
    private String goodsSize;
    private long goodsNum;
    private int status;
    private String createAt;
    private String updateAt;
    private int number;
    private int buyNum;
    private String oriPrice;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Goods))
            return false;
        if (((Goods) obj).get_id().equals(this.get_id()))
            return true;
        else
            return false;
    }
}

package com.myworld.onemall.data.source;

import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Token;

import java.util.List;

/**
 * Created by jianglei on 2016/11/25.
 */

public interface GoodsDataSource {
    public List<Goods> getGoods(int status, int limit, int skip,int type);

    public Token signUp(String phone, String passwd, String code,String deviceToken);

    public Token login(String phone, String passwd,String deviceToken);

    public Token findPD(String userName, String passwd, String code,String deviceToken);

    public boolean getSmsCode(String deviceId,String phoneNumber);

    public List<BannerItem> listBanner();

    public List<Goods> searchGoods(String keywords, int limit, int skip);

    public List<Goods> getSize(String name);

    public Goods goodsDetail(String id);
}

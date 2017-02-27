package com.myworld.onemall.data.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.source.GoodsDataSource;
import com.myworld.onemall.data.source.remote.GoodsRemoteDataSource;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jianglei on 2016/11/25.
 */

public class GoodsRepository implements GoodsDataSource {
    private static GoodsRepository repository;
    private GoodsRemoteDataSource remote;
    private HashMap<String, Goods> cache = new HashMap<>();
    public static final int SORT_TYPE_TIME_DOWN = -1;
    public static final int SORT_TYPE_SOLD_DOWN = -2;
    public static final int SORT_TYPE_PRICE_DOWN = 3;

    public synchronized static GoodsRepository getRepository() {
        if (repository == null)
            repository = new GoodsRepository();
        return repository;
    }

    public GoodsRemoteDataSource getRemote() {
        return remote;
    }

    private GoodsRepository() {
        remote = new GoodsRemoteDataSource();
    }

    @Override
    public List<Goods> getGoods(int status, int limit, int skip, int type) {
        List<Goods> result = remote.getGoods(status, limit, skip, type);
        saveToCache(result);
        return result;
    }

    @Override
    public Token signUp(String phone, String passwd, String code, String deviceToken) {
        return remote.signUp(phone, passwd, code, deviceToken);
    }

    @Override
    public Token login(String phone, String passwd, String deviceToken) {
        return remote.login(phone, passwd, deviceToken);
    }

    @Override
    public Token findPD(String userName, String passwd, String code, String deviceToken) {
        return remote.findPD(userName, passwd, code, deviceToken);
    }

    @Override
    public boolean getSmsCode(String deviceId, String phoneNumber) {
        return remote.getSmsCode(deviceId, phoneNumber);
    }

    @Override
    public List<BannerItem> listBanner() {
        return remote.listBanner();
    }

    @Override
    public List<Goods> searchGoods(String keywords, int limit, int skip) {
        List<Goods> results = remote.searchGoods(keywords, limit, skip);
        saveToCache(results);
        return results;
    }

    @Override
    public List<Goods> getSize(String name) {
        List<Goods> results = remote.getSize(name);
        saveToCache(results);
        return results;
    }

    @Override
    public Goods goodsDetail(String id) {
        Goods goods = getGoods(id);
        if (goods == null) {
            goods = remote.goodsDetail(id);
            if (goods != null)
                cache.put(goods.get_id(), goods);
        }
        return goods;
    }

    public Goods getGoods(String id) {
        return cache.get(id);
    }

    private void saveToCache(List<Goods> goodses) {
        for (Goods goodse : goodses) {
            cache.put(goodse.get_id(), goodse);
        }
    }

    public Picture getFirstPicture(Goods goods) {
        return getGoodsPics(goods).get(0);
    }

    public List<Picture> getGoodsPics(Goods goods) {
        String images = goods.getGoodsPic();
        Gson gson = new Gson();
        List<Picture> pics = gson.fromJson(images, new TypeToken<List<Picture>>() {
        }.getType());
        return pics;
    }
}

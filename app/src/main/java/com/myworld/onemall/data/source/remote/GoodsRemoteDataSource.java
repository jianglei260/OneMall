package com.myworld.onemall.data.source.remote;

import android.util.Log;

import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.source.GoodsDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 2016/11/25.
 */

public class GoodsRemoteDataSource implements GoodsDataSource {
    private ApiService service;

    public GoodsRemoteDataSource() {
        service = RetrofitProvider.create().create(ApiService.class);
    }

    @Override
    public List<Goods> getGoods(int status, int limit, int skip, int type) {
        try {
            return service.getGoodsList(status, limit, skip, type).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ApiService getService() {
        return service;
    }

    @Override
    public Token signUp(String phone, String passwd, String code, String deviceToken) {
        try {
            Log.d(phone, "signUp: ");
            return service.signUp(phone, passwd, code, deviceToken).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Token login(String phone, String passwd, String deviceToken) {
        try {
            return service.login(phone, passwd, deviceToken).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Token findPD(String userName, String passwd, String code, String deviceToken) {
        try {
            return service.findPD(userName, passwd, code, deviceToken).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean getSmsCode(String deviceId, String phoneNumber) {
        try {
            return service.getSmsCode(deviceId, phoneNumber).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<BannerItem> listBanner() {
        try {
            return service.listBanner().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Goods> searchGoods(String keywords, int limit, int skip) {
        try {
            return service.searchGoods(keywords, limit, skip).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Goods> getSize(String name) {
        try {
            return service.getSize(name).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Goods goodsDetail(String id) {
        try {
            return service.goodsDetail(id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

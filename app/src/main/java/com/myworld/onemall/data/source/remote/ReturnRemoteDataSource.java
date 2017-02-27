package com.myworld.onemall.data.source.remote;

import com.myworld.onemall.data.entity.Return;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.source.ReturnDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 2016/12/4.
 */

public class ReturnRemoteDataSource implements ReturnDataSource {
    private ApiService service;

    public ReturnRemoteDataSource() {
        service = GoodsRepository.getRepository().getRemote().getService();
    }

    @Override
    public List<Return> listReturn(String token) {
        try {
            return service.listReturn(token).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteReturn(String token, String orderid) {
        try {
            return service.deleteReturn(token, orderid).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelReturn(String token, String returnid) {
        try {
            return service.cancelReturn(token, returnid).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

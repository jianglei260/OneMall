package com.myworld.onemall.data.repository;

import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.Return;
import com.myworld.onemall.data.source.ReturnDataSource;
import com.myworld.onemall.data.source.remote.OrderRemoteDataSource;
import com.myworld.onemall.data.source.remote.ReturnRemoteDataSource;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jianglei on 2016/12/4.
 */

public class ReturnRepository implements ReturnDataSource {
    private static ReturnRepository repository;
    private ReturnRemoteDataSource remote;
    private HashMap<String, Return> cache;
    public static final int RETURN_STATUS_FINISH = 2;
    public static final int RETURN_STATUS_APPLY = 1;
    public static final int RETURN_STATUS_CANCEL = 0;

    public static synchronized ReturnRepository geRepository() {
        if (repository == null)
            repository = new ReturnRepository();
        return repository;
    }

    private ReturnRepository() {
        remote = new ReturnRemoteDataSource();
        cache = new HashMap<>();
    }

    public void saveToCache(List<Return> returns) {
        for (Return aReturn : returns) {
            cache.put(aReturn.get_id(), aReturn);
        }
    }

    @Override
    public List<Return> listReturn(String token) {
        List<Return> results = remote.listReturn(token);
        saveToCache(results);
        return results;
    }

    @Override
    public boolean deleteReturn(String token, String orderid) {
        boolean result = remote.deleteReturn(token, orderid);
        if (result)
            cache.remove(orderid);
        return result;
    }

    @Override
    public boolean cancelReturn(String token, String returnid) {
        return remote.cancelReturn(token, returnid);
    }
}

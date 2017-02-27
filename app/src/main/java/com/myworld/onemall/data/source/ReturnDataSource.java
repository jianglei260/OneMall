package com.myworld.onemall.data.source;

import com.myworld.onemall.data.entity.Return;

import java.util.List;

/**
 * Created by jianglei on 2016/12/4.
 */

public interface ReturnDataSource {

    public List<Return> listReturn(String token);

    public boolean deleteReturn(String token, String orderid);

    public boolean cancelReturn(String token, String returnid);
}

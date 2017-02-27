package com.myworld.onemall.data.source;

import com.myworld.onemall.data.entity.Search;

import java.util.List;

/**
 * Created by jianglei on 2016/12/7.
 */

public interface SearchDataSource {
    public List<Search> searchHistory(String useerId);

    public void saveSearchWords(String userId, String words);

    public void clearSearch(String userId);

}

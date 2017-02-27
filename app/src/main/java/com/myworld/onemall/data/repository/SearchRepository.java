package com.myworld.onemall.data.repository;

import com.myworld.onemall.data.entity.Search;
import com.myworld.onemall.data.source.SearchDataSource;
import com.myworld.onemall.data.source.local.SearchLocalDataSource;

import java.util.List;

/**
 * Created by jianglei on 2016/12/7.
 */

public class SearchRepository implements SearchDataSource {
    private static SearchRepository repository;
    private SearchLocalDataSource local;

    public static SearchRepository getRepository() {
        if (repository == null)
            repository = new SearchRepository();
        return repository;
    }

    private SearchRepository() {
        local = new SearchLocalDataSource();
    }

    @Override
    public List<Search> searchHistory(String useerId) {
        return local.searchHistory(useerId);
    }

    @Override
    public void saveSearchWords(String userId, String words) {
        local.saveSearchWords(userId, words);
    }

    @Override
    public void clearSearch(String userId) {
        local.clearSearch(userId);
    }
}

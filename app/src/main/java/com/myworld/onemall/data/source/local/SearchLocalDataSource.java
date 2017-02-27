package com.myworld.onemall.data.source.local;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myworld.onemall.base.OneMallApplication;
import com.myworld.onemall.data.entity.Search;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.source.SearchDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jianglei on 2016/12/7.
 */

public class SearchLocalDataSource implements SearchDataSource {

    private List<Search> searches;

    public SearchLocalDataSource() {
        searches = getLocalSearch(OneMallApplication.getInstance());
    }

    @Override
    public List<Search> searchHistory(String useerId) {
        List<Search> results = new ArrayList<>();
        for (Search search : searches) {
            if (search.getUserId().equals(useerId))
                results.add(search);
        }
        return results;
    }

    @Override
    public void saveSearchWords(String userId, String words) {
        boolean contain = false;
        List<Search> results = searchHistory(userId);
        for (Search result : results) {
            if (result.getWords().equals(words)) {
                contain = true;
                result.setTimes(result.getTimes() + 1);
                result.setTime(System.currentTimeMillis());
                break;
            }
        }
        if (!contain) {
            Search search = new Search();
            search.setWords(words);
            search.setTime(System.currentTimeMillis());
            search.setTimes(1);
            search.setUserId(userId);
            searches.add(search);
        }
        saveToLocal(OneMallApplication.getInstance(), searches);
    }

    @Override
    public void clearSearch(String userId) {
        Iterator<Search> iterator = searches.iterator();
        while (iterator.hasNext()) {
            Search search = iterator.next();
            if (search.getUserId().equals(userId))
                iterator.remove();
        }
        saveToLocal(OneMallApplication.getInstance(), searches);
    }

    public List<Search> getLocalSearch(Context context) {
        Gson gson = new Gson();
        String authDataPath = context.getFilesDir().getAbsolutePath() + File.separator + "search.json";
        File authData = new File(authDataPath);
        if (!authData.exists())
            return new ArrayList<>();;
        try {
            Reader reader = new FileReader(authData);
            return gson.fromJson(reader, new TypeToken<List<Search>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveToLocal(Context context, List<Search> searches) {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "search.json";
        Gson gson = new Gson();
        String json = gson.toJson(searches);
        File authData = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(authData, false);
            byte[] data = json.getBytes();
            fos.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

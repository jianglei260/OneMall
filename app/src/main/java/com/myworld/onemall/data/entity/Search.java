package com.myworld.onemall.data.entity;

/**
 * Created by jianglei on 2016/12/7.
 */

public class Search {
    private String words;
    private long time;
    private int times;
    private String userId;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

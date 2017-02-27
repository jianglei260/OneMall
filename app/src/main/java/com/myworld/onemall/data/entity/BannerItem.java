package com.myworld.onemall.data.entity;

/**
 * Created by jianglei on 16/8/10.
 */

public class BannerItem{
    private String url;
    private String pic;
    private String name;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String carouseName) {
        this.name = carouseName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

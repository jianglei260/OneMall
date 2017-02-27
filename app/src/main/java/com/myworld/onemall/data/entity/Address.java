package com.myworld.onemall.data.entity;

/**
 * Created by jianglei on 2016/11/30.
 */

public class Address {
    private String address;
    private String contact;
    private String name;
    private int status;
    private String userId;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Address))
            return false;
        if (obj instanceof Address) {
            Address target = (Address) obj;
            if (target.getName().equals(name) && target.getAddress().equals(address) && target.getContact().equals(contact))
                return true;
            return false;
        }
        return super.equals(obj);
    }
}

package com.myworld.onemall.data.source.remote;

import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.source.AddressDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Field;

/**
 * Created by jianglei on 2016/12/4.
 */

public class AddressRemoteDataSource implements AddressDataSource {

    private ApiService service;

    public AddressRemoteDataSource() {
        service = GoodsRepository.getRepository().getRemote().getService();
    }

    @Override
    public boolean setDefaultAddress(String token, String addressId) {
        try {
            return service.setDefaultAddress(token, addressId).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Address getDefaultAddress(String token) {
        try {
            return service.getDefaultAddress(token).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Address> listAddress(String token) {
        try {
            return service.listAddress(token).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean addAddress(String token, String contact, String address, @Field("name") String name, @Field("status") int status) {
        try {
            return service.addAddress(token, contact, address, name, status).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean editAddress(String token, String contact, String address, String name, int status, String id) {
        try {
            return service.editAddress(token, contact, address, name, status, id).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAddress(String token, String addressId) {
        try {
            return service.deleteAddress(token, addressId).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

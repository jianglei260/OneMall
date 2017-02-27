package com.myworld.onemall.data.source;

import com.myworld.onemall.data.entity.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jianglei on 2016/12/4.
 */

public interface AddressDataSource {

    public boolean setDefaultAddress(String token, String addressId);

    public Address getDefaultAddress(String token);

    public List<Address> listAddress(String token);


    public boolean addAddress(String token, String contact, String address, @Field("name") String name, @Field("status") int status);


    public boolean editAddress(String token, String contact, String address, String name, int status, String id);

    public boolean deleteAddress(String token, String addressId);

}

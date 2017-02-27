package com.myworld.onemall.data.repository;

import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.source.AddressDataSource;
import com.myworld.onemall.data.source.remote.AddressRemoteDataSource;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Field;

/**
 * Created by jianglei on 2016/12/4.
 */

public class AddressRepository implements AddressDataSource {
    private static AddressRepository repository;
    private AddressRemoteDataSource remote;
    private HashMap<String, Address> cache;
    private Address selectAddress;

    public static synchronized AddressRepository getRepository() {
        if (repository == null)
            repository = new AddressRepository();
        return repository;
    }

    private AddressRepository() {
        remote = new AddressRemoteDataSource();
        cache = new HashMap<>();
    }

    public void saveToCache(List<Address> addresses) {
        for (Address address : addresses) {
            cache.put(address.get_id(), address);
        }
    }

    @Override
    public boolean setDefaultAddress(String token, String addressId) {
        boolean result = remote.setDefaultAddress(token, addressId);
        if (result) {
            cache.get(addressId).setStatus(1);
        }
        return result;
    }

    public Address getSelectAddress() {
        return selectAddress;
    }

    public void setSelectAddress(Address selectAddress) {
        this.selectAddress = selectAddress;
    }

    @Override
    public Address getDefaultAddress(String token) {
        return remote.getDefaultAddress(token);
    }

    @Override
    public List<Address> listAddress(String token) {
        List<Address> results = remote.listAddress(token);
        saveToCache(results);
        return results;
    }

    public Address getAddress(String id) {
        return cache.get(id);
    }

    @Override
    public boolean addAddress(String token, String contact, String address, @Field("name") String name, @Field("status") int status) {
        return remote.addAddress(token, contact, address, name, status);
    }

    @Override
    public boolean editAddress(String token, String contact, String address, String name, int status, String id) {
        return remote.editAddress(token, contact, address, name, status, id);
    }

    @Override
    public boolean deleteAddress(String token, String addressId) {
        return remote.deleteAddress(token, addressId);
    }
}

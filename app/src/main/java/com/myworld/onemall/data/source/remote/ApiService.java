package com.myworld.onemall.data.source.remote;

import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.PayInfo;
import com.myworld.onemall.data.entity.Return;
import com.myworld.onemall.data.entity.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jianglei on 2016/11/25.
 */

public interface ApiService {
    @GET("/goods/list")
    Call<List<Goods>> getGoodsList(@Query("status") int status, @Query("limit") int page, @Query("skip") int size, @Query("type")int type);

    @FormUrlEncoded
    @POST("/users/m/signup")
    Call<Token> signUp(@Field("phone") String phone, @Field("password") String password, @Field("code") String code, @Field("devicetoken") String deviceToken);

    @FormUrlEncoded
    @POST("/users/login")
    Call<Token> login(@Field("phoneNum") String phoneNum, @Field("password") String password, @Field("devicetoken") String deviceToken);
    @FormUrlEncoded
    @POST("/users/getsmscode")
    Call<Boolean> getSmsCode(@Field("deviceId") String device, @Field("mobile") String phone);
    @FormUrlEncoded
    @POST("users/m/findpw")
    Call<Token> findPD(@Field("phone") String phoneNum, @Field("password") String password, @Field("code") String code, @Field("devicetoken") String deviceToken);

    @FormUrlEncoded
    @POST("/cart/add ")
    Call<Boolean> add(@Field("token") String token, @Field("goodsId") String goodsId, @Field("num") int num);

    @GET("/cart/list ")
    Call<List<Cart>> getCarts(@Query("token") String token);

    @GET("/cart/list ")
    Call<List<Cart>> getCarts(@Query("token") String token,@Query("status")int stauts);


    @FormUrlEncoded
    @POST("/cart/check ")
    Call<Boolean> check(@Field("token") String token, @Field("cartid") String cartId, @Field("status") int status);

    @FormUrlEncoded
    @POST("/cart/change ")
    Call<Boolean> change(@Field("token") String token, @Field("cartid") String cartId, @Field("num") int num);

    @FormUrlEncoded
    @POST("/cart/edit ")
    Call<Boolean> edit(@Field("token") String token, @Field("cartid") String cartId, @Field("num") int num);

    @FormUrlEncoded
    @POST("/cart/delete ")
    Call<Boolean> delete(@Field("token") String token, @Field("cartid") String cartId);

    @FormUrlEncoded
    @POST("/address/setdefault")
    Call<Boolean> setDefaultAddress(@Field("token") String token, @Field("addressid") String addressId);

    @FormUrlEncoded
    @POST("/address/getdefault")
    Call<Address> getDefaultAddress(@Field("token") String token);

    @GET("/address/list")
    Call<List<Address>> listAddress(@Query("token") String token);

    @FormUrlEncoded
    @POST("/address/add")
    Call<Boolean> addAddress(@Field("token") String token, @Field("contact") String contact, @Field("address") String address, @Field("name") String name, @Field("status") int status);

    @FormUrlEncoded
    @POST("/address/edit")
    Call<Boolean> editAddress(@Field("token") String token, @Field("contact") String contact, @Field("address") String address, @Field("name") String name, @Field("status") int status, @Field("id") String id);

    @FormUrlEncoded
    @POST("/address/delete")
    Call<Boolean> deleteAddress(@Field("token") String token, @Field("addressid") String addressId);

    @FormUrlEncoded
    @POST("/order/add")
    Call<String> addOrder(@Field("token") String token, @Field("name") String name, @Field("contact") String contact, @Field("address") String address, @Field("message") String message, @Field("type") int type);

    @FormUrlEncoded
    @POST("/order/pay")
    Call<String> pay(@Field("token") String token, @Field("id") String orderid);

    @FormUrlEncoded
    @POST("/order/cancel")
    Call<Boolean> cancelOrder(@Field("token") String token, @Field("orderid") String orderid);

    @FormUrlEncoded
    @POST("/order/list")
    Call<List<Order>> listOrder(@Field("token") String token);

    @FormUrlEncoded
    @POST("/order/detail")
    Call<Order> orderDetail(@Field("token") String token, @Field("orderid") String orderid);

    @FormUrlEncoded
    @POST("/order/return")
    Call<Boolean> orderReturn(@Field("token") String token, @Field("orderid") String orderid, @Field("reason") String reason);

    @FormUrlEncoded
    @POST("/order/delete")
    Call<Boolean> deleteOrder(@Field("token") String token, @Field("orderid") String orderid);

    @FormUrlEncoded
    @POST("/return/list")
    Call<List<Return>> listReturn(@Field("token") String token);

    @FormUrlEncoded
    @POST("/return/delete")
    Call<Boolean> deleteReturn(@Field("token") String token, @Field("returnid") String orderid);

    @FormUrlEncoded
    @POST("/return/cancel")
    Call<Boolean> cancelReturn(@Field("token") String token, @Field("returnid") String returnId);

    @GET("/events/list")
    Call<List<BannerItem>> listBanner();

    @GET("/goods/search")
    Call<List<Goods>> searchGoods(@Query("keyword") String keyWords, @Query("limit") int limit, @Query("skip") int skip);

    @GET("goods/getsize")
    Call<List<Goods>> getSize(@Query("name") String name);

    @GET("goods/detail")
    Call<Goods> goodsDetail(@Query("id") String id);
}

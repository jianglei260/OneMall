package com.myworld.onemall.data.source.remote;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myworld.onemall.R;
import com.myworld.onemall.base.OneMallApplication;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.login.LoginActivity;
import com.myworld.onemall.main.MainActivity;
import com.myworld.onemall.settings.SettingActivity;

import java.util.HashMap;

import retrofit2.Retrofit;

/**
 * Created by jianglei on 16/7/11.
 */

public class RetrofitProvider {
    //    //token 状态码
//    600：token为空
//
//    601 ： token过期
//
//    604 ： token不存在
//
////用户表  状态码
//    405：AppKey为空
//
//    406：AppKey无效
//
//    456：国家代码或手机号码为空
//
//    457：手机号码格式错误
//
//    466：请求校验的验证码为空
//
//    467：请求校验验证码频繁（5分钟内同一个appkey的同一个号码最多只能校验三次）
//
//            468：验证码错误
//
//    474：没有打开服务端验证开关
//    500：代码错误
//
//    501：用户未登录
//
//    511：密码太短
//
//    512：密码存在空格
//
//    521：用户已存在
//
//    522：用户不存在
//
//    523：密码错误
//
//    540：登录失败
//
//    541：登出失败
//
////goods 状态码
//    704：缺少参数
//
//    731：缺少商品id
//
//    732：缺少商品status
//
//    734：商品不存在
    //cart 状态码
//    751：缺少购物车 id
//
//    752：商品数量为空
//
//    753：商品数量不能小于1
//
//    754：该条购物车数据不存在
//
////return 状态码
//    701：缺少return id
//
//    702：return状态为空
//
//    703: return不存在
//
////address 状态码
//    761：缺少地址id
//
//    764：地址不存在
//
//    766：没有权限
//
////order 状态码
//    711：缺少订单id
//
//    712：订单status状态为空
//    713：订单不存在
//
//    714：库存不足
//
//    715：退货订单已存在
//
//    716：没有权限
//
//    718：订单已取消，无法再取消
//
//    719：订单已完成，无法取消
//
//    720：订单还没有结束
//
////Coupon状态码
//    771 ： 缺少优惠券id
//
//    774 ： 优惠券不存在
//
//
//
////数据库操作出错  状态码
//    800：数据库操作出错
//
//    801：更新错误
//
//    802：ObjectId格式错误
//
//    803：保存出错
//
//    804：数据库查询出错
//
//    805：数据库删除出错
//
//    806： 没有删除权限
//
//    807：没有删除数据
//
//    808：数据库统计出错
//
//    809：ObjectId为空
    public static HashMap<Integer, String> codeMap = new HashMap<>();

    static {
        codeMap.put(600, "token为空");
        codeMap.put(601, "token过期");
        codeMap.put(604, "token不存在");

        codeMap.put(315, "IP限制");
        codeMap.put(403, "非法操作或没有权限");
        codeMap.put(414, "参数错误");
        codeMap.put(416, "请求太频繁");
        codeMap.put(500, "服务器内部错误");

        codeMap.put(456, "手机号码为空");
        codeMap.put(457, "手机号码格式错误");
        codeMap.put(466, "请求校验的验证码为空");
        codeMap.put(467, "请求校验验证码频繁");
        codeMap.put(468, "验证码错误");
        codeMap.put(474, "没有打开服务端验证开关");

        codeMap.put(500, "服务器内部错误");
        codeMap.put(501, "用户未登录");
        codeMap.put(511, "密码太短");
        codeMap.put(512, "密码存在空格");
        codeMap.put(521, "用户已存在");
        codeMap.put(522, "用户不存在");
        codeMap.put(523, "密码错误");
        codeMap.put(540, "登录失败");
        codeMap.put(541, "登出失败");

        codeMap.put(734, "商品不存在");
        codeMap.put(752, "商品数量为空");
        codeMap.put(753, "商品数量不能小于1");
        codeMap.put(754, "该条购物车数据不存在");

        codeMap.put(764, "地址不存在");
        codeMap.put(713, "订单不存在");
        codeMap.put(714, "库存不足");
        codeMap.put(715, "退货订单已存在");
        codeMap.put(718, "订单已取消，无法再取消");
        codeMap.put(719, "订单已完成，无法取消");
        codeMap.put(720, "订单还没有结束");

        codeMap.put(774, "优惠券不存在");

    }

    public static final String BASE_URl = "http://119.29.141.199";
    public static CodeStatusHandler handler = new CodeStatusHandler() {
        @Override
        public void handleCode(int code, String message) {
            switch (code) {
                case 600:
                case 601:
                case 604:
//                    Intent intent = new Intent(OneMallApplication.getInstance(), LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    OneMallApplication.getInstance().ac
//                    OneMallApplication.getInstance().startActivity(intent);
                    UserRepository.getRepository().deleteToken(OneMallApplication.getInstance());
                    showToast(OneMallApplication.getInstance().getString(R.string.token_not_availble));
                    Intent userout=new Intent(SettingActivity.ACTION_USER_LOGOUT);
                    OneMallApplication.getInstance().sendBroadcast(userout);
                    break;
                default:
                    showToast(codeMap.get(code));
                    break;
            }
        }
    };

    public static void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Intent intent = new Intent(OneMallApplication.ACTION_ERROR_MESSAGE);
            intent.putExtra(OneMallApplication.MESSAGE, message);
            OneMallApplication.getInstance().sendBroadcast(intent);
        }
    }

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new ApiTypeAdapterFactory(handler, "data"))
            .create();

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URl).addConverterFactory(new CustomConverterFactory(handler, gson)).build();
        return retrofit;
    }

    public static Retrofit create(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(new CustomConverterFactory(handler, gson)).build();
        return retrofit;
    }

}

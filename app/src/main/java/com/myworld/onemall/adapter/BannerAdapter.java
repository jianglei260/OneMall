package com.myworld.onemall.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.widget.CustomDraweeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Created by jianglei on 16/8/10.
 */

public class BannerAdapter {
    @BindingAdapter(value = {"urls", "clickListener"}, requireAll = false)
    public static void setEditable(Banner banner, List<BannerItem> urls, OnBannerClickListener listener) {
//        banner.setImages(urls, new OnLoadImageListener() {
//            @Override
//            public void OnLoadImage(ImageView view, Object url) {
//                BannerItem item = (BannerItem) url;
//                Glide.with(view.getContext()).load(item.getPic()).centerCrop().crossFade().into(view);
//            }
//        });

        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, final ImageView imageView) {
                BannerItem item = (BannerItem) path;
//                ((CustomDraweeView)imageView).setImageURI(Uri.parse(item.getPic()));
                ImageRequest request = ImageRequest.fromUri(item.getPic());
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                DataSource<CloseableReference<CloseableImage>>
                        dataSource = imagePipeline.fetchDecodedImage(request, this);
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                    }
                }, UiThreadImmediateExecutorService.getInstance());

//                Glide.with(context).load(item.getPic()).centerCrop().crossFade().into(imageView);
            }
        });
        banner.setImages(urls);
        banner.setOnBannerClickListener(listener);
        banner.setDelayTime(5000);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.start();
    }

}

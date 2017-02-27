package com.myworld.onemall.detail;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.annotationprocessor.ProcessExpressions;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.BR;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.repository.GoodsRepository;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action1;

/**
 * Created by jianglei on 2016/12/14.
 */

public class ImageViewModel extends LoaddingViewModel {
    private ImageActivity activity;
    private Goods goods;
    private int index = 0;
    public final ItemViewSelector<ImageItemViewModel> itemView = new ItemViewSelector<ImageItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ImageItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_image_item);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public ObservableList<ImageItemViewModel> viewModels = new ObservableArrayList<>();

    public ReplyCommand itemSelected = new ReplyCommand(new Action1() {
        @Override
        public void call(Object o) {
            int position = (int) o;
            activity.showSelected(position);
        }
    });

    public ImageViewModel(ImageActivity activity, Goods goods, int index) {
        this.activity = activity;
        this.goods = goods;
        this.index = index;
        initImages(index);
    }


    public void initImages(int index) {
        List<Picture> pictures = GoodsRepository.getRepository().getGoodsPics(goods);
        for (Picture picture : pictures) {
            ImageItemViewModel itemViewModel = new ImageItemViewModel(activity, picture);
            viewModels.add(itemViewModel);
        }
        itemSelected.execute(index);
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}

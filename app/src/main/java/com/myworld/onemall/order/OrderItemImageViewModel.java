package com.myworld.onemall.order;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.base.ItemViewModel;

import rx.functions.Action0;

public class OrderItemImageViewModel {
    public ObservableField<String> imageUrl = new ObservableField<>();
    private ItemViewModel itemViewModel;

    public OrderItemImageViewModel(ItemViewModel itemViewModel) {
        this.itemViewModel = itemViewModel;
    }

    public ReplyCommand itemClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            itemViewModel.itemClick.execute();
        }
    });
}
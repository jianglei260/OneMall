package com.myworld.onemall.main;

import android.databinding.Observable;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.data.entity.Cart;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/8.
 */

public class NumberPickerViewModel {
    public ObservableField<String> number = new ObservableField<>("1");
    private CartFragment fragment;
    public ReplyCommand addClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                int num = Integer.parseInt(number.get());
                number.set(num + 1 + "");
            } catch (NumberFormatException e) {
                number.set("1");
            }

        }
    });

    public ReplyCommand decClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                int num = Integer.parseInt(number.get());
                if (num > 1) {
                    number.set(num - 1 + "");
                }
            } catch (NumberFormatException e) {
                number.set("1");
            }
        }
    });
    public ReplyCommand positiveClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (listenner!=null)
                listenner.selected(true);
            fragment.hideNumberPicker();

        }
    });
    public ReplyCommand negtiveClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (listenner!=null)
                listenner.selected(false);
            fragment.hideNumberPicker();
        }
    });

    public NumberPickerViewModel(CartFragment fragment,int num) {
        this.fragment = fragment;
        number.set(num + "");
        number.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                try {
                    int num = Integer.parseInt(number.get());
                    if (num > 200)
                        number.set("200");
                    if (num <= 0)
                        number.set("1");
                } catch (NumberFormatException e) {
                    number.set("1");
                }
            }
        });
    }

    private SelectedListenner listenner;

    public void setListenner(SelectedListenner listenner) {
        this.listenner = listenner;
    }

    public interface SelectedListenner {
        public void selected(boolean positive);
    }

}

package com.hongdthaui.babysleep.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.hongdthaui.babysleep.model.Song;

/**
 * Created by hongdthaui on 6/19/2020.
 */
public class SongItemViewModel {
    public ObservableField<String> name;
    public ObservableField<String> iconUrl;
    public ObservableField<String> singer;
    public ObservableField<String> timeStr;

    public SongItemViewModel(Song song){
        this.name = new ObservableField<>(song.getName());
        this.iconUrl = new ObservableField<>(song.getIconUrl());
        this.singer = new ObservableField<>(song.getSinger());
        this.timeStr = new ObservableField<>(song.getTimeStr());

    }
    @BindingAdapter("iconUrl")
    public static void loadIcon(ImageView view, String iconUrl) {
        Glide.with(view.getContext())
                .load(iconUrl)
                .into(view);
    }
}

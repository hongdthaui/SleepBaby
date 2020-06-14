package com.hongdthaui.babysleep.model;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

/**
 * Created by hongdthaui on 6/14/2020.
 */
public class SongOnline extends Song {
    public String iconUrl;
    public String audioUrl;
    public SongOnline(){
        super();
    }

    @BindingAdapter("iconUrl")
    public static void loadIcon(ImageView view, String iconUrl) {
        Glide.with(view.getContext())
                .load(iconUrl)
                .into(view);
    }
}

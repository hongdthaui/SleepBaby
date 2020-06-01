package com.hongdthaui.babysleep.view.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdthaui.babysleep.R;

public class SongHolder extends RecyclerView.ViewHolder {
    public ImageView ivSongIcon;
    public TextView tvSongName;
    public TextView tvSinger;
    public TextView tvTime;
    public ObjectAnimator oaSongIcon;

    public SongHolder(@NonNull View itemView) {
        super(itemView);
        ivSongIcon = itemView.findViewById(R.id.item_song_iv_icon);
        tvSongName = itemView.findViewById(R.id.item_song_tv_name);
        tvSinger = itemView.findViewById(R.id.item_song_tv_singer);
        tvTime = itemView.findViewById(R.id.item_song_tv_time);
        oaSongIcon = ObjectAnimator.ofFloat(ivSongIcon,"rotation",0f,360f);
        oaSongIcon.setDuration(5000);
        oaSongIcon.setRepeatCount(ValueAnimator.INFINITE);
        oaSongIcon.setRepeatMode(ValueAnimator.RESTART);
        oaSongIcon.setInterpolator(new LinearInterpolator());
    }

}

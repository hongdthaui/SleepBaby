package com.hongdthaui.babysleep.viewmodel;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.utils.MediaUtils;
import com.hongdthaui.babysleep.view.activity.PlayerActivity;


import java.util.Calendar;

import static com.hongdthaui.babysleep.viewmodel.MainViewModel.MUSIC_SERVICE;

/**
 * Created by hongdthaui on 6/6/2020.
 */
public class PlayerViewModel extends AndroidViewModel {
    private boolean isSeek = false;

    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableField<String> resMoon = new ObservableField<>();
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();
    public ObservableField<String> txtAlarm = new ObservableField<>();
    private ObjectAnimator aniMoon;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
    }

    public void onClickPlay() {
        if (!isPlaying.get()) {
            getMusicService().go();
        } else {
            getMusicService().pausePlayer();
        }
    }

    public void onClickPrev() {
        getMusicService().playPrev();
    }

    public void onClickNext() {
        getMusicService().playNext();
    }

    public void onClickShuffle() {
        getMusicService().setShuffle();
    }

    public void onClickRepeat() {
        getMusicService().setRepeat();
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (isSeek) {
            getMusicService().seek(progress);
            txtCurTime.set(MediaUtils.convertTime(progress));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeek = true;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeek = false;
    }

    public void onClickAlarm(int second) {
        if (second>0)
            getMusicService().setTimeOff(second);
    }

    public void onChangedPlay(Boolean aBoolean) {
        isPlaying.set(aBoolean);
        if (aBoolean) {
            if (aniMoon.isPaused()) {
                aniMoon.resume();
            } else {
                aniMoon.start();
            }
        } else {
            aniMoon.pause();
        }
    }

    public void onChangedRepeat(Boolean aBoolean) {
        resRepeat.set(aBoolean ? R.drawable.ib_repeat_enable : R.drawable.ib_repeat_disable);

    }

    public void onChangedShuffle(Boolean aBoolean) {
        resShuffle.set(aBoolean ? R.drawable.ib_shuffle_enable : R.drawable.ib_shuffle_disable);
    }

    public void onChangedDuration(Integer integer) {
        maxSeekBar.set(integer);
        txtMaxTime.set(MediaUtils.convertTime(integer));
    }

    public void onChangedPosition(Integer integer) {
        progressSeekBar.set(integer);
        txtCurTime.set(MediaUtils.convertTime(integer));
    }

    public MusicService getMusicService() {
        return MUSIC_SERVICE;
    }

    public void onChangedAlarm(Integer integer) {
        txtAlarm.set(MediaUtils.convertTime(integer * 1000));
    }

    public void onChangedIcon(String iconUrl) {
        resMoon.set(iconUrl);
    }

    @BindingAdapter("resMoon")
    public static void loadIconMoon(ImageView view, ObservableField<String> resMoon) {
        Glide.with(view.getContext())
                .load(resMoon.get())
                .into(view);
    }

    public void setAniMoon(ObjectAnimator aniMoon) {
        this.aniMoon = aniMoon;
    }
}

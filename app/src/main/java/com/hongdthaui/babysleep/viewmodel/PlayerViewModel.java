package com.hongdthaui.babysleep.viewmodel;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.app.TimePickerDialog;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;


import java.util.Calendar;

import static com.hongdthaui.babysleep.viewmodel.MainViewModel.MUSIC_SERVICE;

/**
 * Created by hongdthaui on 6/6/2020.
 */
public class PlayerViewModel extends AndroidViewModel {
    public boolean isPlay = false;
    private boolean isSeek = false;

    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableInt resPlay = new ObservableInt(android.R.drawable.ic_media_play);
    public ObservableInt resMoon = new ObservableInt(R.mipmap.ic_song_1);
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();
    public ObservableField<String> txtAlarm = new ObservableField<>();
    public ObjectAnimator aniMoon;
    public PlayerViewModel(@NonNull Application application) {
        super(application);
    }


    public void onClickPlay() {
        if (!isPlay) {
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
            txtCurTime.set(Song.convertTime(progress));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeek = true;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeek = false;
    }
    public void onClickAlarm(int i){
        getMusicService().setTimeOff(i);
        Toast.makeText(getApplication().getApplicationContext(),"Set Alarm success",Toast.LENGTH_LONG).show();
    }
    public void onChangedPlay(Boolean aBoolean) {
        isPlay = aBoolean;
        //resPlay.set(aBoolean?android.R.drawable.ic_media_pause:android.R.drawable.ic_media_play);
        if (isPlay){
            resPlay.set(android.R.drawable.ic_media_pause);
            if (aniMoon.isPaused()) {
                aniMoon.resume();
            } else {
                aniMoon.start();
            }
        }else {
            resPlay.set(android.R.drawable.ic_media_play);
            aniMoon.pause();
        }
    }

    public void onChangedRepeat(Boolean aBoolean) {
        resRepeat.set(aBoolean?R.drawable.ib_repeat_enable:R.drawable.ib_repeat_disable);

    }

    public void onChangedShuffle(Boolean aBoolean) {
        resShuffle.set(aBoolean ? R.drawable.ib_shuffle_enable : R.drawable.ib_shuffle_disable);
    }

    public void onChangedDuration(Integer integer) {
        maxSeekBar.set(integer);
        txtMaxTime.set(Song.convertTime(integer));
    }

    public void onChangedPosition(Integer integer) {
        progressSeekBar.set(integer);
        txtCurTime.set(Song.convertTime(integer));
    }
    public MusicService getMusicService() {
        return MUSIC_SERVICE;
    }

    public void onChangedAlarm(Integer integer) {
        txtAlarm.set(Song.convertTime(integer*1000));
    }

    public void onChangedIcon(Integer icon) {
        resMoon.set(icon);
    }

    public void setAniMoon(ObjectAnimator aniMoon) {
        this.aniMoon = aniMoon;
    }
}

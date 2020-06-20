package com.hongdthaui.babysleep.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;


import com.hongdthaui.babysleep.R;

import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.utils.MediaUtils;
import com.hongdthaui.babysleep.view.activity.PlayerActivity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    public static MusicService MUSIC_SERVICE;
    private Context context;

    private boolean bound = false;
    private boolean isSeek = false;

    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean(true);

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            MUSIC_SERVICE = musicBinder.getService();
            //musicService.setListSong(songList);
            bound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };
    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }




    public void onPlay(int index) {
        MUSIC_SERVICE.setIndexSong(index);
        MUSIC_SERVICE.playSong();
    }

    public void onClickPlay() {
        if (!isPlaying.get()) {
            MUSIC_SERVICE.go();
        } else {
            MUSIC_SERVICE.pausePlayer();
        }

    }
    public void onClickPrev() {
        MUSIC_SERVICE.playPrev();
    }

    public void onClickNext() {
        MUSIC_SERVICE.playNext();
    }

    public void onClickShuffle() {
        MUSIC_SERVICE.setShuffle();
    }

    public void onClickRepeat() {
        MUSIC_SERVICE.setRepeat();
    }

    public void onClickControl(View view){
        Intent intent = new Intent(view.getContext(), PlayerActivity.class);
        view.getContext().startActivity(intent);
    }
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (isSeek) {
            MUSIC_SERVICE.seek(progress);
            txtCurTime.set(MediaUtils.convertTime(progress));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeek = true;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeek = false;
    }

    public void onChangedPlay(Boolean aBoolean) {
        isPlaying.set(aBoolean);
    }

    public void onChangedRepeat(Boolean aBoolean) {
        resRepeat.set(aBoolean?R.drawable.ib_repeat_enable:R.drawable.ib_repeat_disable);

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

    public void setSongList(List<Song> songList) {
        MUSIC_SERVICE.setListSong(songList);
    }



    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public MusicService getMusicService() {
        return MUSIC_SERVICE;
    }

    public boolean isBound() {
        return bound;
    }

}

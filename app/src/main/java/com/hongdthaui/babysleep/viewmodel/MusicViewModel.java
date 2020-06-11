package com.hongdthaui.babysleep.viewmodel;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;


import com.hongdthaui.babysleep.R;

import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.model.SongData;
import com.hongdthaui.babysleep.service.MusicService;

import java.util.List;

public class MusicViewModel extends AndroidViewModel {
    public static MusicService MUSIC_SERVICE;
    private Context context;

    private List<Song> songList;
    private List<Song> northList;
    private List<Song> southList;
    private List<Song> wordlessList;

    private boolean bound = false;
    private boolean isPlay = false;
    private boolean isSeek = false;

    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableInt resPlay = new ObservableInt(android.R.drawable.ic_media_play);
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();

    //private Handler threadHandler = new Handler();
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
    public MusicViewModel(@NonNull Application application) {
        super(application);
        Log.e("MUSIC","new MusicViewModel");
        context = application.getApplicationContext();
        northList = SongData.createNorthList(context);
        southList = SongData.createSouthList(context);
        wordlessList = SongData.createWordlessList(context);
    }

    public void onPlay(int index) {
        MUSIC_SERVICE.setIndexSong(index);
        MUSIC_SERVICE.playSong();
    }

    public void onClickPlay() {
        if (songList == null) {
            Toast.makeText(context, R.string.notice_no_song, Toast.LENGTH_LONG).show();
            return;
        }
        if (!isPlay) {
            //isPlay = true;
            //resPlay.set(android.R.drawable.ic_media_pause);
            MUSIC_SERVICE.go();
        } else {
            //isPlay = false;
            //resPlay.set(android.R.drawable.ic_media_play);
            MUSIC_SERVICE.pausePlayer();
        }
    }
    public void onClickPrev() {
        if (songList == null) {
            return;
        }
        MUSIC_SERVICE.playPrev();
    }

    public void onClickNext() {
        if (songList == null) {
            return;
        }
        MUSIC_SERVICE.playNext();
    }

    public void onClickShuffle() {
        MUSIC_SERVICE.setShuffle();
    }

    public void onClickRepeat() {
        MUSIC_SERVICE.setRepeat();
    }
    public void onClickControl(){

    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (isSeek) {
            MUSIC_SERVICE.seek(progress);
            txtCurTime.set(Song.convertTime(progress));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeek = true;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeek = false;
    }

    public void onChangedPlay(Boolean aBoolean) {
        isPlay = aBoolean;
        resPlay.set(aBoolean?android.R.drawable.ic_media_pause:android.R.drawable.ic_media_play);
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
    public List<Song> getNorthList() {
        return northList;
    }

    public List<Song> getSouthList() {
        return southList;
    }

    public List<Song> getWordlessList() {
        return wordlessList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        MUSIC_SERVICE.setListSong(songList);
    }

    public int getCurrentPosition() {
        if (MUSIC_SERVICE != null && bound)
            return MUSIC_SERVICE.getPosition().getValue();
        else
            return 0;
    }

    public int getDuration() {
        if (MUSIC_SERVICE != null && bound)
            return MUSIC_SERVICE.getDuration().getValue();
        else
            return 0;
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

/*    private class UpdateSeekBar implements Runnable {
        int index;
        public UpdateSeekBar(int index){
            this.index = index;
        }
        @Override
        public void run() {
            if (!isSeek) {
                int current = getCurrentPosition();
                int total = getDuration();
                txtMaxTime.set(Song.convertTime(total));
                progressSeekBar.set(current);
                maxSeekBar.set(total);
                txtCurTime.set(Song.convertTime(current));
                Log.e("MUSIC","UpdateSeekBar run ="+this.index);
            }
            threadHandler.postDelayed(this, 500);
        }
    }*/
}

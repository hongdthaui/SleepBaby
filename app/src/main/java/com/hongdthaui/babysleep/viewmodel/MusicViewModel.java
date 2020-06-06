package com.hongdthaui.babysleep.viewmodel;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
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
import com.hongdthaui.babysleep.view.adapter.SongAdapter;

import java.util.List;

public class MusicViewModel extends AndroidViewModel {
    public static MusicService MUSIC_SERVICE;
    private Context context;

    public List<Song> songList;
    private List<Song> northList;
    private List<Song> southList;
    private List<Song> wordlessList;
    public List<SongAdapter.SongHolder> songHolders;

    public boolean bound = false;
    public boolean isPlay = false;
    private boolean isSeek = false;

    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableInt resPlay = new ObservableInt(android.R.drawable.ic_media_play);
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();

    private ObjectAnimator nowRotation;
    private Handler threadHandler = new Handler();
    public MusicViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        northList = SongData.createNorthList(context);
        southList = SongData.createSouthList(context);
        wordlessList = SongData.createWordlessList(context);
    }

    public void onPlay(int index) {
        isPlay = true;
        resPlay.set(android.R.drawable.ic_media_pause);

        MUSIC_SERVICE.setViewModel(this);
        MUSIC_SERVICE.setIndexSong(index);
        MUSIC_SERVICE.playSong();

        UpdateSeekBar updateSeekBar = new UpdateSeekBar();
        threadHandler.postDelayed(updateSeekBar, 500);
    }

    public void onClickPlay() {
        if (songList == null) {
            Toast.makeText(context, R.string.notice_no_song, Toast.LENGTH_LONG).show();
            return;
        }
        if (!isPlay) {
            isPlay = true;
            resPlay.set(android.R.drawable.ic_media_pause);
            startOrResumRotation(this.nowRotation);
            MUSIC_SERVICE.go();
        } else {
            isPlay = false;
            resPlay.set(android.R.drawable.ic_media_play);
            pauseRotation(nowRotation);
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
        resShuffle.set(MUSIC_SERVICE.isShuffle().getValue() ? R.drawable.ib_shuffle_disable : R.drawable.ib_shuffle_enable);
        MUSIC_SERVICE.setShuffle();
    }

    public void onClickRepeat() {
        resRepeat.set(MUSIC_SERVICE.isRepeat().getValue()?R.drawable.ib_repeat_disable:R.drawable.ib_repeat_enable);
        MUSIC_SERVICE.setRepeat();
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

    public List<Song> getNorthList() {
        return northList;
    }

    public List<Song> getSouthList() {
        return southList;
    }

    public List<Song> getWordlessList() {
        return wordlessList;
    }

    public void activeRotation() {
       // Log.e("MUSIC","activeRotation");
        if (songHolders == null) return;
       // Log.e("MUSIC","songHolders=="+songHolders.size());
        SongAdapter.SongHolder songHolder = songHolders.get(MUSIC_SERVICE.getIndexSong());

        pauseRotation(this.nowRotation);
        this.nowRotation = songHolder.oaSongIcon;
        startOrResumRotation(this.nowRotation);
    }

    public void pauseRotation(ObjectAnimator animator) {
        if (animator != null) {
            animator.pause();
        }
    }

    public void startOrResumRotation(ObjectAnimator animator) {
        if (animator.isPaused())
            animator.resume();
        else
            animator.start();
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        MUSIC_SERVICE.setListSong(songList);
    }

    public int getCurrentPosition() {
        if (MUSIC_SERVICE != null && bound)
            return MUSIC_SERVICE.getPosition();
        else
            return 0;
    }

    public int getDuration() {
        if (MUSIC_SERVICE != null && bound)
            return MUSIC_SERVICE.getDuration();
        else
            return 0;
    }

    private class UpdateSeekBar implements Runnable {
        @Override
        public void run() {
            if (!isSeek) {
                int current = getCurrentPosition();
                int total = getDuration();
                txtMaxTime.set(Song.convertTime(total));
                progressSeekBar.set(current);
                maxSeekBar.set(total);
                txtCurTime.set(Song.convertTime(current));
            }
            threadHandler.postDelayed(this, 500);
        }
    }
}

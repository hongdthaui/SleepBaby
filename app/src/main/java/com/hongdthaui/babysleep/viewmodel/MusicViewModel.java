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
    public MusicService musicService;
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

        musicService.setViewModel(this);
        musicService.setIndexSong(index);
        musicService.playSong();

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
            musicService.go();
        } else {
            isPlay = false;
            resPlay.set(android.R.drawable.ic_media_play);
            pauseRotation(nowRotation);
            musicService.pausePlayer();
        }
    }

    public void onClickPrev() {
        musicService.playPrev();
    }

    public void onClickNext() {
        musicService.playNext();
    }

    public void onClickShuffle() {
        resShuffle.set(musicService.isShuffle() ? R.drawable.ib_shuffle_disable : R.drawable.ib_shuffle_enable);
        musicService.setShuffle();
    }

    public void onClickRepeat() {
        resRepeat.set(musicService.isRepeat()?R.drawable.ib_repeat_disable:R.drawable.ib_repeat_enable);
        musicService.setRepeat();
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (isSeek) {
            musicService.seek(progress);
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
        if (songHolders == null) return;
        SongAdapter.SongHolder songHolder = songHolders.get(musicService.getIndexSong());

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
        musicService.setListSong(songList);
    }

    public int getCurrentPosition() {
        if (musicService != null && bound)
            return musicService.getPosition();
        else
            return 0;
    }

    public int getDuration() {
        if (musicService != null && bound)
            return musicService.getDuration();
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

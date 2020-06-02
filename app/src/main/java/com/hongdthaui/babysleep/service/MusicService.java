package com.hongdthaui.babysleep.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.viewmodel.MusicViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer player;
    private List<Song> listSong;
    private int indexSong;
    private IBinder binder = new MusicBinder();
    private boolean shuffle = false;
    private boolean repeat = false;
    private MusicViewModel viewModel;

    public MusicService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Log.e("MUSIC","onBind Service");

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
       // Log.e("MUSIC","onUnbind Service");

        //player.stop();
       // player.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.e("MUSIC","onCreate Service");
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        indexSong = 0;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  playSong();
        return START_STICKY;
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        //if (mp.getCurrentPosition() == 0) {
            mp.reset();
            playNext();
       // }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }


    public void setListSong(List<Song> songs) {
        this.listSong = songs;
    }

    public void setIndexSong(int indexSong) {
        this.indexSong = indexSong;
    }
    public int getIndexSong() {
        return indexSong;
    }

    public void setViewModel(MusicViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void playSong() {
        player.reset();
        //Log.i("MUSIC", "songPosn=" + songPosn);
        Song playSong = listSong.get(indexSong);
        int id = getApplicationContext().getResources().getIdentifier(playSong.raw, "raw", getApplicationContext().getPackageName());
        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + id);
        Log.i("MUSIC", "data source id=" + id);
        try {
            player.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            Log.e("MUSIC", "Error setting data source", e);
        }
        player.prepareAsync();
        this.viewModel.activeRotation();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void playNext() {
        if (!repeat) {
            if (shuffle) {
                Random ran = new Random();
                int nextSong = indexSong;
                while (nextSong == indexSong) {
                    nextSong = ran.nextInt(listSong.size());
                }
                indexSong = nextSong - 1;
            } else {
                indexSong++;
                if (indexSong >= listSong.size()) indexSong = 0;
            }
        }
        playSong();
    }

    public void playPrev() {
        indexSong--;
        if (indexSong < 0) {
            indexSong = 0;
        }
        playSong();
    }

    public void setShuffle() {
        shuffle = !shuffle;
    }
    public boolean isShuffle(){
        return shuffle;
    }
    public void setRepeat() {
        repeat = !repeat;
    }
    public boolean isRepeat(){
        return repeat;
    }
    public int getPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}

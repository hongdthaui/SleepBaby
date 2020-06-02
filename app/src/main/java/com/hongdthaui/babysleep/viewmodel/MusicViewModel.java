package com.hongdthaui.babysleep.viewmodel;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hongdthaui.babysleep.MainActivity;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityMainBinding;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicViewModel extends AndroidViewModel {
    public MusicService musicService;
    private List<Song> northList = new ArrayList<>();
    private List<Song> southList = new ArrayList<>();
    private List<Song> wordlessList = new ArrayList<>();
    private Context context;
    public MutableLiveData<Boolean> isPlay = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShuffle = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRepeat =  new MutableLiveData<>();
    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableInt resPlay = new ObservableInt(android.R.drawable.ic_media_play);
    private ObjectAnimator nowRotation;
    public List<Song> songList;
    public List<SongAdapter.SongHolder> songHolders;
    public MusicViewModel(@NonNull Application application) {
        super(application);
        Log.e("MUCSIC","new MusicViewModel ");
        this.context = application.getApplicationContext();
        createNorthList();
        createSouthList();
        createWordlessList();
        isPlay.setValue(false);
        isShuffle.setValue(false);
        isRepeat.setValue(false);

    }


    public void onClickPlay(){
        if (songList==null){
            Toast.makeText(context,R.string.notice_no_song,Toast.LENGTH_LONG).show();
            return;
        }
        if(!isPlay.getValue()) {
            isPlay.setValue(true);
            resPlay.set(android.R.drawable.ic_media_pause);
            startOrResumRotation(this.nowRotation);

            musicService.go();

        }
        else {
            isPlay.setValue(false);
            resPlay.set(android.R.drawable.ic_media_play);
            pauseRotation(this.nowRotation);
            musicService.pausePlayer();
        }
    }
    public void onClickPrev(){
        musicService.playPrev();
    }
    public void onClickNext(){
        musicService.playNext();
    }
    public void onClickShuffle() {

        if(isShuffle.getValue()){
            isShuffle.setValue(false);

            resShuffle.set(R.drawable.ib_shuffle_disable);
            //mainBinding.activityMainIbShuffle.setImageResource(R.drawable.ib_shuffle_disable);
        }else {
            isShuffle.setValue(true);
            resShuffle.set(R.drawable.ib_shuffle_enable);
            //mainBinding.activityMainIbShuffle.setImageResource(R.drawable.ib_shuffle_enable);

        }
        musicService.setShuffle();
    }
    public void onClickRepeat() {
        if(isRepeat.getValue()){
            isRepeat.setValue(false);
            resRepeat.set(R.drawable.ib_repeat_disable);
        }else {
            isRepeat.setValue(true);
            resRepeat.set(R.drawable.ib_repeat_enable);
        }
        musicService.setRepeat();
    }
    public void createNorthList() {
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Pi ca chu", R.mipmap.song_icon, "Anh Thơ", context, "picachiu"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Pi ca chu", R.mipmap.song_icon, "Anh Thơ", context, "picachiu"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
    }
    public void createSouthList() {
        southList.add(new Song("Mẹ yêu con",R.mipmap.song_icon,"Hiền Thục",context,"south1"));
        southList.add(new Song("Cái cò các vạc",R.mipmap.song_icon,"Anh Thơ",context,"south2"));
    }
    public void createWordlessList() {
        wordlessList.add(new Song("Mẹ yêu con",R.mipmap.song_icon,"Hiền Thục",context,"wordless1"));
        wordlessList.add(new Song("Cái cò các vạc",R.mipmap.song_icon,"Anh Thơ",context,"wordless2"));
        wordlessList.add(new Song("Con cò bé bé",R.mipmap.song_icon,"Hiền Thục",context,"wordless3"));
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

    public void activeRotation(ObjectAnimator nowRotation) {
        pauseRotation(this.nowRotation);
        this.nowRotation = nowRotation;
        startOrResumRotation(this.nowRotation);
    }
    public void pauseRotation(ObjectAnimator animator) {
        if (animator != null) {
            animator.pause();
        }
    }
    public void startOrResumRotation(ObjectAnimator animator){
        if (animator.isPaused())
            animator.resume();
        else
            animator.start();
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        musicService.setListSong(songList);
    }

}

package com.hongdthaui.babysleep.viewmodel;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableChar;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hongdthaui.babysleep.MainActivity;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityMainBinding;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.model.SongData;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.adapter.PagerAdapter;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicViewModel extends AndroidViewModel {
    public MusicService musicService;
    public boolean bound = false;
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
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();
    private ObjectAnimator nowRotation;
    public List<Song> songList;
    public List<SongAdapter.SongHolder> songHolders;
    private Song songRotation;
    private boolean isSeek = false;
    private Handler threadHandler = new Handler();

    public MusicViewModel(@NonNull Application application) {
        super(application);
        Log.e("MUCSIC","new MusicViewModel ");
        this.context = application.getApplicationContext();
        northList = SongData.createNorthList(context);
        southList = SongData.createSouthList(context);
        wordlessList = SongData.createWordlessList(context);
        isPlay.setValue(false);
        isShuffle.setValue(false);
        isRepeat.setValue(false);

    }
    public void onPlay(int index){
        isPlay.setValue(true);
        resPlay.set(android.R.drawable.ic_media_pause);

        musicService.setViewModel(this);
        musicService.setIndexSong(index);
        musicService.playSong();
        UpdateSeekBar updateSeekBar = new UpdateSeekBar();
        threadHandler.postDelayed(updateSeekBar,500);

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
            pauseRotation();
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (isSeek) {
             // Log.e("MUSIC", "progress=" + seekBar.getProgress());
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
//        Log.e("MUSIC","songRotation--------------songHolders="+songHolders.size());
        if (songHolders==null) return;
        SongAdapter.SongHolder songHolder = songHolders.get(musicService.getIndexSong());
      //  activeRotation(songHolder.oaSongIcon);

        pauseRotation();
        this.nowRotation = songHolder.oaSongIcon;
        startOrResumRotation(this.nowRotation);
    }
    public void pauseRotation() {
        if (this.nowRotation != null) {
            this.nowRotation.pause();
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
    public int getCurrentPosition() {
        if (musicService !=null&&bound)
            return musicService.getPosn();
        else
            return 0;
    }
    public int getDuration() {
        if (musicService !=null&&bound)
            return musicService.getDuration();
        else
            return 0;
    }

    private class UpdateSeekBar implements Runnable{

        @Override
        public void run() {
            if (!isSeek) {
                int current = getCurrentPosition();
                //Log.e("MUSIC","current=="+current);
                int total = getDuration();
                txtMaxTime.set(Song.convertTime(total));
                progressSeekBar.set(current);
                maxSeekBar.set(total);
                txtCurTime.set(Song.convertTime(current));
            }
            threadHandler.postDelayed(this,500);
        }
    }
}

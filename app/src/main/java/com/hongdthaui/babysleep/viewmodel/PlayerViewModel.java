package com.hongdthaui.babysleep.viewmodel;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;

import static com.hongdthaui.babysleep.viewmodel.MusicViewModel.MUSIC_SERVICE;

/**
 * Created by hongdthaui on 6/6/2020.
 */
public class PlayerViewModel extends AndroidViewModel {
    public boolean isPlay = false;
    private boolean isSeek = false;

    public ObservableInt resShuffle = new ObservableInt(R.drawable.ib_shuffle_disable);
    public ObservableInt resRepeat = new ObservableInt(R.drawable.ib_repeat_disable);
    public ObservableInt resPlay = new ObservableInt(android.R.drawable.ic_media_play);
    public ObservableInt progressSeekBar = new ObservableInt(0);
    public ObservableInt maxSeekBar = new ObservableInt(0);
    public ObservableField<String> txtCurTime = new ObservableField<>();
    public ObservableField<String> txtMaxTime = new ObservableField<>();


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
    public MusicService getMusicService() {
        return MUSIC_SERVICE;
    }
/*    oaSongIcon = ObjectAnimator.ofFloat(itemSongBinding.itemSongIvIcon,"rotation",0f,90f);
            oaSongIcon.setDuration(5000);
            oaSongIcon.setRepeatCount(ValueAnimator.INFINITE);
            oaSongIcon.setRepeatMode(ValueAnimator.RESTART);
            oaSongIcon.setInterpolator(new LinearInterpolator());*/
/*    public void activeRotation() {
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
    }*/

}

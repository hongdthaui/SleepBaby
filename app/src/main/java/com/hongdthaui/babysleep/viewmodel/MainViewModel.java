package com.hongdthaui.babysleep.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hongdthaui.babysleep.R;

import com.hongdthaui.babysleep.firebase.FirebaseQuery;
import com.hongdthaui.babysleep.model.SongOnline;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.utils.MediaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewModel extends AndroidViewModel {
    public static MusicService MUSIC_SERVICE;
    private Context context;

    private List<SongOnline> songList;
    private MutableLiveData<List<SongOnline>> northList = new MutableLiveData<>();
    private MutableLiveData<List<SongOnline>> southList =  new MutableLiveData<>();
    private MutableLiveData<List<SongOnline>> wordlessList =  new MutableLiveData<>();

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
        //Log.e("MUSIC","new MusicViewModel");
        getNorthSongList();
        getSouthSongList();
        getWordlessSongList();
    }

    private void getNorthSongList() {
        FirebaseQuery.getNorthSongList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<HashMap<String, SongOnline>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, SongOnline>>() {
                    };
                    Map<String, SongOnline> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                    if (objectHashMap != null) {
                        List<SongOnline> songOnlineList = new ArrayList<>(objectHashMap.values());
                       northList.setValue(songOnlineList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getSouthSongList() {
        FirebaseQuery.getSouthSongList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<HashMap<String, SongOnline>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, SongOnline>>() {
                    };
                    Map<String, SongOnline> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                    if (objectHashMap != null) {
                        List<SongOnline> songOnlineList = new ArrayList<>(objectHashMap.values());
                        southList.setValue(songOnlineList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getWordlessSongList() {
        FirebaseQuery.getWordlessSongList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<HashMap<String, SongOnline>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, SongOnline>>() {
                    };
                    Map<String, SongOnline> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                    if (objectHashMap != null) {
                        List<SongOnline> songOnlineList = new ArrayList<>(objectHashMap.values());
                        wordlessList.setValue(songOnlineList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        txtMaxTime.set(MediaUtils.convertTime(integer));
    }

    public void onChangedPosition(Integer integer) {
        progressSeekBar.set(integer);
        txtCurTime.set(MediaUtils.convertTime(integer));
    }
    public LiveData<List<SongOnline>> getNorthList() {
        return northList;
    }

    public LiveData<List<SongOnline>> getSouthList() {
        return southList;
    }

    public LiveData<List<SongOnline>> getWordlessList() {
        return wordlessList;
    }

    public void setSongList(List<SongOnline> songList) {
        this.songList = songList;
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

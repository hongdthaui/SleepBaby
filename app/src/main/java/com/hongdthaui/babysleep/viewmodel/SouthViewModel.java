package com.hongdthaui.babysleep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hongdthaui.babysleep.firebase.FirebaseQuery;
import com.hongdthaui.babysleep.model.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongdthaui on 6/16/2020.
 */
public class SouthViewModel extends AndroidViewModel {
    private MutableLiveData<List<Song>> southList;
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public SouthViewModel(@NonNull Application application) {
        super(application);
        southList = new MutableLiveData<>();
        intSouthSongList();
    }

    public void intSouthSongList() {
        FirebaseQuery.getSouthSongList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<HashMap<String, Song>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Song>>() {
                    };
                    Map<String, Song> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                    if (objectHashMap != null) {
                        List<Song> songOnlineList = new ArrayList<>(objectHashMap.values());
                        southList.setValue(songOnlineList);
                        isLoading.set(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<Song>> getSouthList() {
        return southList;
    }
}

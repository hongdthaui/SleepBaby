package com.hongdthaui.babysleep.viewmodel;

import android.app.Application;
import android.icu.util.VersionInfo;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hongdthaui.babysleep.firebase.FirebaseQuery;
import com.hongdthaui.babysleep.model.SongOnline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongdthaui on 6/16/2020.
 */
public class WorldlessViewModel extends AndroidViewModel {
    private MutableLiveData<List<SongOnline>> wordlessList;
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public WorldlessViewModel(@NonNull Application application) {
        super(application);
        wordlessList = new MutableLiveData<>();
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
                        isLoading.set(false);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<SongOnline>> getWordlessList() {
        return wordlessList;
    }
}

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
import com.hongdthaui.babysleep.model.SongOnline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongdthaui on 6/16/2020.
 */
public class CentralViewModel extends AndroidViewModel {
    private MutableLiveData<List<SongOnline>> centralList;
    public ObservableBoolean isLoading = new ObservableBoolean(true);

    public CentralViewModel(@NonNull Application application) {
        super(application);
        centralList = new MutableLiveData<>();
        initCentralSongList();
    }

    public void initCentralSongList() {
        FirebaseQuery.getCentralSongList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<HashMap<String, SongOnline>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, SongOnline>>() {
                    };
                    Map<String, SongOnline> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                    if (objectHashMap != null) {
                        List<SongOnline> songOnlineList = new ArrayList<>(objectHashMap.values());
                        centralList.setValue(songOnlineList);
                        isLoading.set(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<SongOnline>> getCentralList() {
        return centralList;
    }
}
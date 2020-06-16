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
import com.hongdthaui.babysleep.databinding.ActivityMainBindingImpl;
import com.hongdthaui.babysleep.firebase.FirebaseQuery;
import com.hongdthaui.babysleep.model.SongOnline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongdthaui on 6/16/2020.
 */
public class NorthViewModel extends AndroidViewModel {
    private MutableLiveData<List<SongOnline>> northList;
    public ObservableBoolean isLoading = new ObservableBoolean(true);

    public NorthViewModel(@NonNull Application application) {
        super(application);
        northList = new MutableLiveData<>();
        initNorthSongList();
    }

    private void initNorthSongList() {
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
                        isLoading.set(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<SongOnline>> getNorthList() {
        return northList;
    }
}

package com.hongdthaui.babysleep.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hongdthaui.babysleep.R;

import java.util.ArrayList;
import java.util.List;

public class SongData {
    public static LiveData<List<Song>> getNorthList(Context context) {
        final MutableLiveData<List<Song>> northList = new MutableLiveData<>();
        List<Song> list = new ArrayList<>();
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_1, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Pi ca chu", R.mipmap.ic_song_2, "Anh Thơ", context, "picachiu"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_3, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_4, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_1, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Pi ca chu", R.mipmap.ic_song_2, "Anh Thơ", context, "picachiu"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_3, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_4, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_1, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Pi ca chu", R.mipmap.ic_song_2, "Anh Thơ", context, "picachiu"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_3, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_4, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_1, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Pi ca chu", R.mipmap.ic_song_2, "Anh Thơ", context, "picachiu"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_3, "Hiền Thục", context, "ghencovy"));
        list.add(new Song("Ghen cô vy", R.mipmap.ic_song_4, "Hiền Thục", context, "ghencovy"));
        northList.setValue(list);
        return northList;
    }
    public static LiveData<List<Song>> getSouthList(Context context) {
        final MutableLiveData<List<Song>> southList = new MutableLiveData<>();
        List<Song> list = new ArrayList<>();
        list.add(new Song("Mẹ yêu con",R.mipmap.song_icon,"Hiền Thục",context,"south1"));
        list.add(new Song("Cái cò các vạc",R.mipmap.song_icon,"Anh Thơ",context,"south2"));
        southList.setValue(list);
        return  southList;
    }
    public static LiveData<List<Song>> getWordlessList(Context context) {
        final MutableLiveData<List<Song>> wordlessList = new MutableLiveData<>();
        List<Song> list = new ArrayList<>();
        list.add(new Song("Mẹ yêu con",R.mipmap.song_icon,"Hiền Thục",context,"wordless1"));
        list.add(new Song("Cái cò các vạc",R.mipmap.song_icon,"Anh Thơ",context,"wordless2"));
        list.add(new Song("Con cò bé bé",R.mipmap.song_icon,"Hiền Thục",context,"wordless3"));
        wordlessList.setValue(list);
        return wordlessList;
    }
}

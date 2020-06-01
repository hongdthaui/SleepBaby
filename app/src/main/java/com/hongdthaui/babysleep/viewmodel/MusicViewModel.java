package com.hongdthaui.babysleep.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicViewModel extends ViewModel {
    private List<Song> northList = new ArrayList<>();
    private List<Song> southList = new ArrayList<>();
    private List<Song> wordlessList = new ArrayList<>();
    private Context context;

    public void init(Context context) {
        this.context = context;
        createNorthList();
        createSouthList();
        createWordlessList();
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
}

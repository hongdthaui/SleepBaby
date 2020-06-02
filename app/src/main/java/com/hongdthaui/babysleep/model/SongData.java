package com.hongdthaui.babysleep.model;

import android.content.Context;

import com.hongdthaui.babysleep.R;

import java.util.ArrayList;
import java.util.List;

public class SongData {
    public static List<Song> createNorthList(Context context) {
        List<Song> northList = new ArrayList<>();
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Pi ca chu", R.mipmap.song_icon, "Anh Thơ", context, "picachiu"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Pi ca chu", R.mipmap.song_icon, "Anh Thơ", context, "picachiu"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        northList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", context, "ghencovy"));
        return northList;
    }
    public static List<Song> createSouthList(Context context) {
        List<Song> southList = new ArrayList<>();
        southList.add(new Song("Mẹ yêu con",R.mipmap.song_icon,"Hiền Thục",context,"south1"));
        southList.add(new Song("Cái cò các vạc",R.mipmap.song_icon,"Anh Thơ",context,"south2"));
        return  southList;
    }
    public static List<Song> createWordlessList(Context context) {
        List<Song> wordlessList = new ArrayList<>();
        wordlessList.add(new Song("Mẹ yêu con",R.mipmap.song_icon,"Hiền Thục",context,"wordless1"));
        wordlessList.add(new Song("Cái cò các vạc",R.mipmap.song_icon,"Anh Thơ",context,"wordless2"));
        wordlessList.add(new Song("Con cò bé bé",R.mipmap.song_icon,"Hiền Thục",context,"wordless3"));
        return wordlessList;
    }
}

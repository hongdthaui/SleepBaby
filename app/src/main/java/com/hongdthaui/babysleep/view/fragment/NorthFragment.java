package com.hongdthaui.babysleep.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdthaui.babysleep.MainActivity;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.utils.ItemClickSupport;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;
import com.hongdthaui.babysleep.view.adapter.SongHolder;

import java.util.ArrayList;
import java.util.List;


public class NorthFragment extends Fragment {
    private RecyclerView rvNorthList;

    public NorthFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_north, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainActivity activity = (MainActivity) getActivity();
        final List<Song> songList = new ArrayList<>();


        songList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", getContext(), "ghencovy"));
        songList.add(new Song("Pi ca chu", R.mipmap.song_icon, "Anh Thơ", getContext(), "picachiu"));
        songList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", getContext(), "ghencovy"));
        songList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", getContext(), "ghencovy"));
        songList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", getContext(), "ghencovy"));
        songList.add(new Song("Pi ca chu", R.mipmap.song_icon, "Anh Thơ", getContext(), "picachiu"));
        songList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", getContext(), "ghencovy"));
        songList.add(new Song("Ghen cô vy", R.mipmap.song_icon, "Hiền Thục", getContext(), "ghencovy"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        final SongAdapter songAdapter = new SongAdapter(songList);
        rvNorthList = view.findViewById(R.id.fragment_north_rv);
        rvNorthList.setAdapter(songAdapter);
        rvNorthList.setLayoutManager(linearLayoutManager);


        ItemClickSupport.addTo(rvNorthList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                List<SongHolder> songHolders = new ArrayList<>();

                for (int i=0;i<songList.size();i++) {
                    SongHolder songHolder = (SongHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    songHolders.add(songHolder);
                }

                //SongHolder songHolder = (SongHolder) recyclerView.findViewHolderForAdapterPosition(position);
                //activity.activeRotation(songHolder.oaSongIcon);

                activity.setSongList(songList);
                activity.setSongHolders(songHolders);
                activity.onPlay(position);



            }
        });

    }

}

package com.hongdthaui.babysleep.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.hongdthaui.babysleep.utils.ItemClickSupport;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;

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
        final List<Song> songList = activity.musicViewModel.getNorthList();
       // Log.e("MUSIC","onViewCreated");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        final SongAdapter songAdapter = new SongAdapter(songList);
        rvNorthList = view.findViewById(R.id.fragment_north_rv);
        rvNorthList.setAdapter(songAdapter);
        rvNorthList.setLayoutManager(linearLayoutManager);


        ItemClickSupport.addTo(rvNorthList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                activity.onPlay(position,songList);
            }
        });

    }
}

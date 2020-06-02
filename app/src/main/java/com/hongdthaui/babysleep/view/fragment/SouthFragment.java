package com.hongdthaui.babysleep.view.fragment;

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
import com.hongdthaui.babysleep.utils.ItemClickSupport;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class SouthFragment extends Fragment {
    private RecyclerView rvSouthList;
    public SouthFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_south,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainActivity activity = (MainActivity) getActivity();
        final List<Song> songList = activity.musicViewModel.getSouthList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        SongAdapter songAdapter = new SongAdapter(songList);
        rvSouthList = view.findViewById(R.id.fragment_south_rv);
        rvSouthList.setAdapter(songAdapter);
        rvSouthList.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(rvSouthList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //SongHolder songHolder = (SongHolder) recyclerView.findViewHolderForAdapterPosition(position);
               // activity.activeRotation(songHolder.oaSongIcon);

                List<SongAdapter.SongHolder> songHolders = new ArrayList<>();

                for (int i=0;i<songList.size();i++) {
                    SongAdapter.SongHolder songHolder = (SongAdapter.SongHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    songHolders.add(songHolder);
                }

                activity.setSongList(songList);
                activity.setSongHolders(songHolders);
                activity.onPlay(position);


            }
        });
    }
}

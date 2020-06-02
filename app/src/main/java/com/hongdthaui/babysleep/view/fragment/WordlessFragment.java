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

public class WordlessFragment extends Fragment {
    private RecyclerView rvWordlessList;
    public WordlessFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wordless,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainActivity activity = (MainActivity) getActivity();
        final List<Song> songList = activity.musicViewModel.getWordlessList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        SongAdapter songAdapter = new SongAdapter(songList);
        rvWordlessList = view.findViewById(R.id.fragment_wordless_rv);
        rvWordlessList.setAdapter(songAdapter);
        rvWordlessList.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(rvWordlessList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //SongHolder songHolder = (SongHolder) recyclerView.findViewHolderForAdapterPosition(position);
                //activity.activeRotation(songHolder.oaSongIcon);
                List<SongAdapter.SongHolder> songHolders = new ArrayList<>();
                for (int i=0;i<songList.size();i++) {
                    SongAdapter.SongHolder songHolder = (SongAdapter.SongHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    songHolders.add(songHolder);
                }

                activity.musicViewModel.setSongList(songList);
                activity.musicViewModel.songHolders=songHolders;
                activity.onPlay(position);
            }
        });
    }
}

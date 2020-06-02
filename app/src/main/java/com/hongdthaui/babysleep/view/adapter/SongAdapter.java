package com.hongdthaui.babysleep.view.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ItemSongBinding;
import com.hongdthaui.babysleep.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private List<Song> songList;
    public SongAdapter(List<Song> songs){
        this.songList = songs;
    }
    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding itemSongBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_song,parent,false);
        return new SongHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.itemSongBinding.setSong(songList.get(position));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }



    public class SongHolder extends RecyclerView.ViewHolder {

        public ItemSongBinding itemSongBinding;
        public ObjectAnimator oaSongIcon;

        public SongHolder(@NonNull ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.itemSongBinding = itemSongBinding;

            oaSongIcon = ObjectAnimator.ofFloat(itemSongBinding.itemSongIvIcon,"rotation",0f,360f);
            oaSongIcon.setDuration(5000);
            oaSongIcon.setRepeatCount(ValueAnimator.INFINITE);
            oaSongIcon.setRepeatMode(ValueAnimator.RESTART);
            oaSongIcon.setInterpolator(new LinearInterpolator());
        }

    }
}

package com.hongdthaui.babysleep.view.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ItemSongBinding;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.model.SongOnline;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private List<SongOnline> songList = new ArrayList<>();
    private ViewGroup viewGroup;
    public SongAdapter(){}
    public SongAdapter(List<SongOnline> songs){
        this.songList = songs;
    }
    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding itemSongBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_song,parent,false);
        viewGroup = parent;
        return new SongHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.itemSongBinding.setSong(songList.get(position));
        //Log.e("MUSIC",""+songList.get(position).iconUrl);
        //Glide.with(viewGroup).load(songList.get(position).iconUrl).into(holder.itemSongBinding.itemSongIvIcon);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setSongList(List<SongOnline> songList) {
        this.songList = songList;
    }

    public List<SongOnline> getSongList() {
        return songList;
    }

    public class SongHolder extends RecyclerView.ViewHolder {

        public ItemSongBinding itemSongBinding;

        public SongHolder(@NonNull ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.itemSongBinding = itemSongBinding;
        }

    }
}

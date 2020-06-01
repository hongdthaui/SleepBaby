package com.hongdthaui.babysleep.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongHolder> {
    private List<Song> songList;
    public SongAdapter(List<Song> songs){
        this.songList = songs;
    }
    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        Song song = songList.get(position);
        holder.ivSongIcon.setImageResource(song.getIcon());
        holder.tvSongName.setText(song.getName());
        holder.tvSinger.setText(song.getSinger());
        holder.tvTime.setText(convertTime(song.getTime()));


        //holder.oaSongIcon.start();
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    public String convertTime(long time){
        time = time/1000;
        long second = time%60;
        long minute = time/60;
        if (second<10) {
            return String.valueOf(minute) + ":0" + String.valueOf(second);
        }
        return String.valueOf(minute) + ":" + String.valueOf(second);
    }
}

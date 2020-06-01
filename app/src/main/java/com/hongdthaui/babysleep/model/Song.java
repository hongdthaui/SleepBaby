package com.hongdthaui.babysleep.model;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;

public class Song {
    private String name;
    private int icon;
    private String singer;
    private long time;
    private String raw;

    public Song() {
    }

    ;

    public Song(String name, int icon, String singer, long time, String raw) {
        this.name = name;
        this.icon = icon;
        this.singer = singer;
        this.time = time;
        this.raw = raw;
    }

    public Song(String name, int icon, String singer, Context context, String raw) {
        this.name = name;
        this.icon = icon;
        this.singer = singer;
        this.raw = raw;
        this.time = getDuration(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public long getDuration(Context context) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int id = context.getResources().getIdentifier(this.raw, "raw", context.getPackageName());
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
        mediaMetadataRetriever.setDataSource(context, uri);
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationStr);
    }
}

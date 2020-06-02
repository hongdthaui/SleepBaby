package com.hongdthaui.babysleep.model;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;


public class Song {
    public String name;
    public int icon;
    public String singer;
    public long time;
    public String txtTime;
    public String raw;

    public Song() {
    }
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
        this.txtTime = convertTime(this.time);
    }

    public long getDuration(Context context) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int id = context.getResources().getIdentifier(this.raw, "raw", context.getPackageName());
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
        mediaMetadataRetriever.setDataSource(context, uri);
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationStr);
    }

    public static String convertTime(long time){
        time = time/1000;
        long second = time%60;
        long minute = time/60;
        if (second<10) {
            return String.valueOf(minute) + ":0" + String.valueOf(second);
        }
        return String.valueOf(minute) + ":" + String.valueOf(second);
    }
}

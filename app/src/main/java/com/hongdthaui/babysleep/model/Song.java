package com.hongdthaui.babysleep.model;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;


public class Song {
    private String name;
    private String singer;
    private String timeStr;
    private String iconUrl;
    private String audioUrl;

    public Song() {
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}

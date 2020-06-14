package com.hongdthaui.babysleep.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hongdthaui on 6/13/2020.
 */
public class MediaUtils {
    public static String LOG_TAG = "BabySleep";
    public static long getDuration(Context context, String raw) {
        Log.e(LOG_TAG,"raw="+ raw);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int id = context.getResources().getIdentifier(raw, "raw", context.getPackageName());
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
        mediaMetadataRetriever.setDataSource(context, uri);
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationStr);
    }

    public static String convertTime(long time){
        time = time/1000;
        long second = time%60;
        long minute = (time/60)%60;
        long hour = time/3600;

        String secondStr = String.valueOf(second);
        String minuteStr = String.valueOf(minute);
        if (second<10) {
            secondStr = "0" + second;
        }
        if (hour>0){
            if (minute<10){
                minuteStr = "0"+minute;
            }
            return hour+":"+ minuteStr + ":" + secondStr;
        }
        return minuteStr + ":" + secondStr;
    }
}

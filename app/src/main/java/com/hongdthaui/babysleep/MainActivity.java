package com.hongdthaui.babysleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hongdthaui.babysleep.databinding.ActivityMainBinding;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.adapter.PagerAdapter;
import com.hongdthaui.babysleep.view.adapter.SongHolder;
import com.hongdthaui.babysleep.viewmodel.MusicViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    public MusicViewModel musicViewModel;
    private ImageButton ibPlay;
    private ImageButton ibNext;
    private ImageButton ibPrev;
    private ImageButton ibShuffle;
    private ImageButton ibRepeat;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SeekBar seekBar;
    private TextView tvMaxTime;
    private TextView tvCurTime;
    private MusicService musicService;
    private List<Song> songList;
    private List<SongHolder> songHolders;
    private Intent intent;
    private ObjectAnimator nowRotation;
    private boolean isSeek = false;
    private boolean bound = false;
    private boolean isPlay = false;
    //private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean fisrtPlay = true;
    private Handler threadHandler = new Handler();
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
           // Log.e("MUSIC","songList==="+songList.size());
            //musicService.setListSong(songList);
            bound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //setContentView(R.layout.activity_main);
        //musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        //musicViewModel.init(getApplicationContext());
        musicViewModel = new MusicViewModel(this);
        activityMainBinding.setMusicViewModel(musicViewModel);
        musicViewModel.isShuffle.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (musicService!=null&&bound)
                    musicService.setShuffle();
            }
        });

        ibPlay = (ImageButton) findViewById(R.id.activity_main_ib_play);
        ibNext = (ImageButton) findViewById(R.id.activity_main_ib_next);
        ibPrev = (ImageButton) findViewById(R.id.activity_main_ib_prev);
        ibRepeat = (ImageButton) findViewById(R.id.activity_main_ib_repeat);
        //ibShuffle = (ImageButton) findViewById(R.id.activity_main_ib_shuffle);
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.activity_main_tabLayout);
        seekBar = (SeekBar) findViewById(R.id.activity_main_seekBar);
        tvMaxTime = (TextView) findViewById(R.id.activity_main_tv_maxTime);
        tvCurTime = (TextView) findViewById(R.id.activity_main_tv_curTime);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlay();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNext();
            }
        });
        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPrev();
            }
        });
/*        ibShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickShuffle();
            }
        });*/
        ibRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRepeat();
            }
        });
        seekBar.setClickable(true);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSeek) {
                  //  Log.e("MUSIC", "progress=" + seekBar.getProgress());
                    seekTo(progress);
                    tvCurTime.setText(convertTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeek = false;
            }
        });
    }

    private void clickRepeat() {
        if(isRepeat){
            isRepeat = false;
            ibRepeat.setImageResource(R.drawable.ib_repeat_disable);
        }else {
            isRepeat = true;
            ibRepeat.setImageResource(R.drawable.ib_repeat_enable);
        }
        musicService.setRepeat();
    }

/*
    private void clickShuffle() {
        if(isShuffle){
            isShuffle = false;
            ibShuffle.setImageResource(R.drawable.ib_shuffle_disable);
        }else {
            isShuffle = true;
            ibShuffle.setImageResource(R.drawable.ib_shuffle_enable);
        }
        musicService.setShuffle();
    }
*/

    public void clickPlay(){
        if (songList==null){
            Toast.makeText(this,"Xin mời chọn bài hát",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isPlay) {
            isPlay = true;
            ibPlay.setImageResource(android.R.drawable.ic_media_pause);
            startOrResumRotation(this.nowRotation);

            start();
        }
        else {
            isPlay = false;
            ibPlay.setImageResource(android.R.drawable.ic_media_play);
            pauseRotation(this.nowRotation);
            pause();
        }
    }
    public void clickNext(){
        musicService.playNext();
    }
    public void clickPrev(){
        musicService.playPrev();
    }
    public void onPlay(int index){
        isPlay = true;
        ibPlay.setImageResource(android.R.drawable.ic_media_pause);

        //Log.e("MUSIC SERVICE", "index..."+index);
        Song song = songList.get(index);
        tvMaxTime.setText(convertTime(song.getTime()));
        seekBar.setMax((int) song.getTime());
        musicService.setIndexSong(index);
        musicService.playSong();

        if (fisrtPlay){
            fisrtPlay = false;
            musicService.getNowSong().observe(this, new Observer<Song>() {
                @Override
                public void onChanged(Song song) {
                    //Log.e("MUSIC", "Playing changed...musicService.getPosn()="+musicService.getIndexSong());
                    SongHolder songHolder = songHolders.get(musicService.getIndexSong());
                    activeRotation(songHolder.oaSongIcon);
                }
            });
        }

        UpdateSeekBar updateSeekBar = new UpdateSeekBar();
        threadHandler.postDelayed(updateSeekBar,500);

    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        musicService.setListSong(songList);
    }

    public void setSongHolders(List<SongHolder> songHolders) {
        this.songHolders = songHolders;
       // Log.e("MUSIC", "setSongHolders..."+songHolders.size());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (intent == null) {
            //Log.e("MUSIC SERVICE", "Playing...");
            intent = new Intent(MainActivity.this, MusicService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_timer:
                return true;
            case R.id.menu_about:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicService !=null&&bound&&musicService.isPng())
            return musicService.getDuration();
         else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService !=null&&bound)
            return musicService.getPosn();
        else
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && bound)
            return musicService.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void activeRotation(ObjectAnimator nowRotation) {
        pauseRotation(this.nowRotation);
        this.nowRotation = nowRotation;
        startOrResumRotation(this.nowRotation);
    }
    public void pauseRotation(ObjectAnimator animator) {
        if (animator != null) {
            animator.pause();
        }
    }
    public void startOrResumRotation(ObjectAnimator animator){
        if (animator.isPaused())
            animator.resume();
        else
            animator.start();
    }
    private class UpdateSeekBar implements Runnable{

        @Override
        public void run() {
            if (!isSeek) {
                int current = getCurrentPosition();
                //Log.e("MUSIC","current=="+current);
                seekBar.setProgress(current);
                tvCurTime.setText(convertTime(current));
            }
            threadHandler.postDelayed(this,500);
        }
    }
    public String convertTime(long miniSecond){
        long minute = miniSecond/60000;
        long second = miniSecond/1000%60;
        if (second<10){
            return (minute+":0"+second);
        }
        else {
            return (minute + ":" + second);
        }
    }
}

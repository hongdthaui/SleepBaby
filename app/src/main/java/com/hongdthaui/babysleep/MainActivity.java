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
import android.os.PersistableBundle;
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
import com.hongdthaui.babysleep.view.adapter.SongAdapter;
import com.hongdthaui.babysleep.viewmodel.MusicViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    public MusicViewModel musicViewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SeekBar seekBar;
    private TextView tvMaxTime;
    private TextView tvCurTime;



    private Intent intent;
    private boolean isSeek = false;
    private boolean bound = false;
    private boolean fisrtPlay = true;
    private Handler threadHandler = new Handler();
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicViewModel.musicService = musicBinder.getService();
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
        //musicViewModel = new MusicViewModel(this);
        musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        activityMainBinding.setMusicViewModel(musicViewModel);

        viewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.activity_main_tabLayout);

        seekBar = (SeekBar) findViewById(R.id.activity_main_seekBar);
        tvMaxTime = (TextView) findViewById(R.id.activity_main_tv_maxTime);
        tvCurTime = (TextView) findViewById(R.id.activity_main_tv_curTime);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setUpOnListener();
    }


    private void setUpOnListener() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSeek) {
                    //  Log.e("MUSIC", "progress=" + seekBar.getProgress());
                    seekTo(progress);
                    tvCurTime.setText(Song.convertTime(progress));
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

    public void onPlay(int index){
        musicViewModel.isPlay.setValue(true);
        musicViewModel.resPlay.set(android.R.drawable.ic_media_pause);

        musicViewModel.musicService.setIndexSong(index);
        musicViewModel.musicService.playSong();

        if (fisrtPlay){
            fisrtPlay = false;
            musicViewModel.musicService.getNowSong().observe(this, new Observer<Song>() {
                @Override
                public void onChanged(Song song) {
                    //Log.e("MUSIC", "Playing changed...musicService.getPosn()="+musicService.getIndexSong());
                    SongAdapter.SongHolder songHolder = musicViewModel.songHolders.get(musicViewModel.musicService.getIndexSong());
                    musicViewModel.activeRotation(songHolder.oaSongIcon);
                }
            });
        }

        MainActivity.UpdateSeekBar updateSeekBar = new MainActivity.UpdateSeekBar();
        threadHandler.postDelayed(updateSeekBar,500);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (intent == null) {
            Log.e("MUSIC SERVICE", "Playing...");
            intent = new Intent(MainActivity.this, MusicService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
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
        musicViewModel.musicService.go();
    }

    @Override
    public void pause() {
        musicViewModel.musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicViewModel.musicService !=null&&bound&&musicViewModel.musicService.isPng())
            return musicViewModel.musicService.getDuration();
         else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicViewModel.musicService !=null&&bound)
            return musicViewModel.musicService.getPosn();
        else
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicViewModel.musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicViewModel.musicService != null && bound)
            return musicViewModel.musicService.isPng();
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

    private class UpdateSeekBar implements Runnable{

        @Override
        public void run() {
            if (!isSeek) {
                int current = getCurrentPosition();
                //Log.e("MUSIC","current=="+current);
                int total = getDuration();
                seekBar.setMax(total);
                tvMaxTime.setText(Song.convertTime(total));
                seekBar.setProgress(current);
                tvCurTime.setText(Song.convertTime(current));
            }
            threadHandler.postDelayed(this,500);
        }
    }
}

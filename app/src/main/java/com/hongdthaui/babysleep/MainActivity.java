package com.hongdthaui.babysleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;


import com.google.android.material.tabs.TabLayout;
import com.hongdthaui.babysleep.databinding.ActivityMainBinding;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.activity.PlayerActivity;
import com.hongdthaui.babysleep.view.adapter.PagerAdapter;
import com.hongdthaui.babysleep.viewmodel.MusicViewModel;
import static com.hongdthaui.babysleep.viewmodel.MusicViewModel.MUSIC_SERVICE;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    public MusicViewModel musicViewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ConstraintLayout constraintLayout;

    private Intent intent;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            MUSIC_SERVICE = musicBinder.getService();
            //musicService.setListSong(songList);
            musicViewModel.bound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicViewModel.bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        activityMainBinding.setMusicViewModel(musicViewModel);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.activity_main_tabLayout);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void onPlay(){
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (intent == null) {
            //Log.e("MUSIC SERVICE", "Playing...");
            intent = new Intent(MainActivity.this, MusicService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            //startService(intent);
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        musicViewModel.activeRotation();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout.setBackground(getDrawable(R.drawable.bg_land));
        } else {
            constraintLayout.setBackground(getDrawable(R.drawable.bg));
        }
    }

    @Override
    public void start() {
        MUSIC_SERVICE.go();
    }

    @Override
    public void pause() {
        MUSIC_SERVICE.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (MUSIC_SERVICE !=null&&musicViewModel.bound&&MUSIC_SERVICE.isPlaying().getValue())
            return MUSIC_SERVICE.getDuration();
         else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (MUSIC_SERVICE !=null&&musicViewModel.bound)
            return MUSIC_SERVICE.getPosition();
        else
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        MUSIC_SERVICE.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (MUSIC_SERVICE != null && musicViewModel.bound)
            return MUSIC_SERVICE.isPlaying().getValue();
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

}

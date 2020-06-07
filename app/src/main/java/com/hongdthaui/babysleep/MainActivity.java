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
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.activity.PlayerActivity;
import com.hongdthaui.babysleep.view.adapter.PagerAdapter;
import com.hongdthaui.babysleep.viewmodel.MusicViewModel;

import java.util.List;

import static com.hongdthaui.babysleep.viewmodel.MusicViewModel.MUSIC_SERVICE;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    public MusicViewModel musicViewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ConstraintLayout constraintLayout;

    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        activityMainBinding.setMusicViewModel(musicViewModel);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        constraintLayout = findViewById(R.id.constraintLayout);
        viewPager = findViewById(R.id.activity_main_viewPager);
        tabLayout = findViewById(R.id.activity_main_tabLayout);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void onPlay(int curSong, List<Song> songList){
        musicViewModel.setSongList(songList);
        musicViewModel.onPlay(curSong);
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (intent == null) {
            //Log.e("MUSIC SERVICE", "Playing...");
            intent = new Intent(MainActivity.this, MusicService.class);
            bindService(intent, musicViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
            //startService(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(musicViewModel.getServiceConnection());
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
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout.setBackground(getDrawable(R.drawable.bg_land));
        } else {
            constraintLayout.setBackground(getDrawable(R.drawable.bg));
        }
    }

    @Override
    public void start() {
        musicViewModel.getMusicService().go();
    }

    @Override
    public void pause() {
        musicViewModel.getMusicService().pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicViewModel.getMusicService() !=null&&musicViewModel.isBound()&&musicViewModel.getMusicService().isPlaying().getValue())
            return musicViewModel.getMusicService().getDuration().getValue();
         else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicViewModel.getMusicService() !=null&&musicViewModel.isBound())
            return musicViewModel.getMusicService().getPosition().getValue();
        else
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicViewModel.getMusicService().seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicViewModel.getMusicService() != null && musicViewModel.isBound())
            return musicViewModel.getMusicService().isPlaying().getValue();
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

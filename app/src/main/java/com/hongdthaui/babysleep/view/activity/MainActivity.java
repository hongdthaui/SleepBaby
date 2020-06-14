package com.hongdthaui.babysleep.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityMainBinding;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.model.SongOnline;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.adapter.PagerAdapter;
import com.hongdthaui.babysleep.viewmodel.MainViewModel;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Boolean firstPlay = true;
    private Intent intent;
    private LinearLayout llControl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        activityMainBinding.setViewModel(viewModel);

        llControl = findViewById(R.id.activity_main_ll_control);
        viewPager = findViewById(R.id.activity_main_viewPager);
        tabLayout = findViewById(R.id.activity_main_tabLayout);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        llControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstPlay){
                    Toast.makeText(MainActivity.this, R.string.notice_no_song, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onPlay(int curSong, List<SongOnline> songList){
        viewModel.setSongList(songList);
        viewModel.onPlay(curSong);
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
        if (firstPlay){
            setUpListener();
            firstPlay = false;
        }
    }

    private void setUpListener() {
        viewModel.getMusicService().isPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewModel.onChangedPlay(aBoolean);
            }
        });
        viewModel.getMusicService().isRepeat().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewModel.onChangedRepeat(aBoolean);
            }
        });
        viewModel.getMusicService().isShuffle().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewModel.onChangedShuffle(aBoolean);
            }
        });
        viewModel.getMusicService().getDuration().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                viewModel.onChangedDuration(integer);
            }
        });
        viewModel.getMusicService().getPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                viewModel.onChangedPosition(integer);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (intent == null) {
            //Log.e("MUSIC SERVICE", "Playing...");
            intent = new Intent(MainActivity.this, MusicService.class);
            bindService(intent, viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
            //startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(viewModel.getServiceConnection());
    }
    public MainViewModel getViewModel() {
        return viewModel;
    }
}

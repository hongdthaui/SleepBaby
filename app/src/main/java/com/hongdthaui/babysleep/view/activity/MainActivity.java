package com.hongdthaui.babysleep.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityMainBinding;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.service.MusicService;
import com.hongdthaui.babysleep.view.adapter.PagerAdapter;
import com.hongdthaui.babysleep.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    ActivityMainBinding mainBinding;
    private Boolean firstPlay = true;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainBinding.setViewModel(viewModel);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mainBinding.activityMainViewPager.setAdapter(pagerAdapter);
        mainBinding.activityMainTabLayout.setupWithViewPager(mainBinding.activityMainViewPager);

        mainBinding.activityMainLlControl.setVisibility(View.GONE);
        mainBinding.activityMainLlControl.setOnClickListener(new View.OnClickListener() {
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
    public void onPlay(int curSong, List<Song> songList){
        viewModel.setSongList(songList);
        viewModel.onPlay(curSong);
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
        if (firstPlay){
            mainBinding.activityMainLlControl.setVisibility(View.VISIBLE);
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
            intent = new Intent(MainActivity.this, MusicService.class);
            bindService(intent, viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
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

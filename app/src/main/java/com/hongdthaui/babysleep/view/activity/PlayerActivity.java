package com.hongdthaui.babysleep.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityPlayerBinding;
import com.hongdthaui.babysleep.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_player);
        final ActivityPlayerBinding playerBinding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        playerBinding.setPlayerViewModel(playerViewModel);

    }

    @Override
    protected void onStart() {
        super.onStart();
        playerViewModel.getMusicService().isPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                playerViewModel.onChangedPlay(aBoolean);
            }
        });
        playerViewModel.getMusicService().isRepeat().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                playerViewModel.onChangedRepeat(aBoolean);
            }
        });
        playerViewModel.getMusicService().isShuffle().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                playerViewModel.onChangedShuffle(aBoolean);
            }
        });
        playerViewModel.getMusicService().getDuration().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                playerViewModel.onChangedDuration(integer);
            }
        });
        playerViewModel.getMusicService().getPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                playerViewModel.onChangedPosition(integer);
            }
        });
    }
}

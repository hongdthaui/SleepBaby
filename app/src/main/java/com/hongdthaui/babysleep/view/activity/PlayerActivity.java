package com.hongdthaui.babysleep.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.TimePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TimePicker;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityPlayerBinding;
import com.hongdthaui.babysleep.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    private ActivityPlayerBinding playerBinding;
    ObjectAnimator aniMoon;
    private int hour = 0;
    private int minutes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerBinding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        playerBinding.setPlayerViewModel(playerViewModel);

        initAlarm();
        initMoon();


    }

    public void initAlarm(){

        playerBinding.activityPlayerTvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hour = hourOfDay;
                        minutes = minute;
                        playerViewModel.onClickAlarm(hour*3600+minute*60);
                        //tvAlarm.setText(hour+":"+minutes+":00");
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(PlayerActivity.this,android.R.style.Theme_Material_Light_Dialog_Alert,
                        onTimeSetListener, hour, minutes, true);
                timePickerDialog.setTitle(R.string.title_alarm);
                timePickerDialog.show();
            }
        });
    }
    public void initMoon(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        aniMoon = ObjectAnimator.ofFloat(playerBinding.activityPlayerIvMoon, "translationY", 0, -size.y+200);
        aniMoon.setDuration(20000);
        aniMoon.setRepeatCount(ValueAnimator.INFINITE);
        aniMoon.setRepeatMode(ValueAnimator.REVERSE);
        playerViewModel.setAniMoon(aniMoon);
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
        playerViewModel.getMusicService().getTimeOff().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                playerViewModel.onChangedAlarm(integer);
            }
        });
        playerViewModel.getMusicService().getIconSong().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                playerViewModel.onChangedIcon(integer);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

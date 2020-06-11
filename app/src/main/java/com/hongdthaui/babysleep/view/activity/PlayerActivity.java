package com.hongdthaui.babysleep.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.TimePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.databinding.ActivityPlayerBinding;
import com.hongdthaui.babysleep.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    private ImageView imageView;
    private TextView tvAlarm;
    private int hour = 0;
    private int minutes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_player);
        final ActivityPlayerBinding playerBinding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        playerBinding.setPlayerViewModel(playerViewModel);



/*        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_moon);
        animation.setDuration(100);
        imageView.startAnimation(animation);*/

        /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"tran",0f,90f);
        objectAnimator.setDuration(5000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());*/
        tvAlarm = findViewById(R.id.activity_player_tv_alarm);
        tvAlarm.setOnClickListener(new View.OnClickListener() {
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


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int duration = playerViewModel.getMusicService().getDuration().getValue()/2;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);

        imageView = findViewById(R.id.activity_player_iv_moon);
        ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(imageView, "translationY", 0, -height+200);
        scaleAnimY.setDuration(duration);
        scaleAnimY.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimY.setRepeatMode(ValueAnimator.REVERSE);
// Setup scale X
        ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(imageView, "translationY", 0, height-200);
        scaleAnimX.setDuration(duration);
        scaleAnimX.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimX.setRepeatMode(ValueAnimator.REVERSE);
// Setup animation set
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleAnimX, scaleAnimY);
        set.start();
        playerViewModel.setAnimator(set);
    }
}

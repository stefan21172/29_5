package com.example.mplayer_stefan;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    public MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.roundabout);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        startService(new Intent(MainActivity.this,myservice.class));
    handler=new Handler();

    seekBar=(SeekBar)findViewById(R.id.seekBar2);


    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            seekBar.setMax(mediaPlayer.getDuration());
            playCycle();
            mediaPlayer.start();
        }
    });



    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
        if(input){
            mediaPlayer.seekTo(progress);
        }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    }
    public void playCycle(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying()){
            runnable=new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }

    public void stop(View v){
        seekBar.setProgress(0);
       mediaPlayer.seekTo(0);
       mediaPlayer.pause();
        stopService(new Intent(MainActivity.this,myservice.class));
    }
    public void play_pause(View v) {
        if (mediaPlayer.isPlaying()){
               mediaPlayer.pause();

    }
        else {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            mediaPlayer.start();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        handler.removeCallbacks(runnable);
    }
}

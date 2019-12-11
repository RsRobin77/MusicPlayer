package com.blood.donate.musicplayer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MUSIC_REQ = 1;
    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp, mediaPlayer;
    int totalTime;
    Uri myUri;
    TextView TitleText;
    private Button listpage;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);
        TitleText = findViewById(R.id.tvsongsTitle);
        listpage = findViewById(R.id.listpage);
        listpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this, AllSongs.class);
                startActivityForResult(i,MUSIC_REQ);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SONGINFO", MODE_PRIVATE);
        String temptitle=pref.getString("SongTitle", "");
        String tempuri = pref.getString("SongLocation", "");

        setMusic(temptitle,tempuri);



/*
        // Media Player
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(.5f, .5f);
        totalTime = mp.getDuration();
*/

        // Position Bar
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        // Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }

    }

    private void setMusic(String titleText, String tempuri) {

        TitleText.setText(titleText);
        try {
            myUri = Uri.parse(tempuri);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), myUri);

            mediaPlayer.prepare();
            totalTime = mediaPlayer.getDuration();
            mediaPlayer.setVolume(.5f, .5f);
            mediaPlayer.seekTo(0
            );

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {

        musicStatus();


    }

    private void musicStatus() {
        if (!mediaPlayer.isPlaying()) {
            // Stopping
            mediaPlayer.start();
            //     mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);

        } else {
            // Playing
            mediaPlayer.pause();
            //   mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== MUSIC_REQ && resultCode==RESULT_OK){
            mediaPlayer.reset();

            Toast.makeText(this, "Back from list", Toast.LENGTH_SHORT).show();
            String location=data.getStringExtra("path");
            String song=data.getStringExtra("title");
            setMusic(song,location);

            musicStatus();
        }
    }
}
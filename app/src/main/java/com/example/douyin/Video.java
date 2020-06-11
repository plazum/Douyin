package com.example.douyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        final VideoView videoview = findViewById(R.id.videoView);
        Intent intent = getIntent();
        String text = intent.getStringExtra("url");
        videoview.setVideoPath(text);
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        videoview.setOnClickListener(new View.OnClickListener() {
            private boolean pause = false;

            @Override
            public void onClick(View v) {
                if (pause) {
                    pause = false;
                    videoview.start();
                } else {
                    pause = true;
                    videoview.pause();
                }
            }
        });
        videoview.start();
    }
}
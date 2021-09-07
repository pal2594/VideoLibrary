package com.example.videolibrary.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videolibrary.R;

public class SingleVideoView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_view);

        VideoView playing_video = findViewById(R.id.videoView2);

        Intent intent = getIntent();
        String video_to_play = intent.getStringExtra("video_path");

        playing_video.setVideoURI(Uri.parse(video_to_play));
        playing_video.requestFocus();
        playing_video.start();


    }
}
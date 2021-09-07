package com.example.videolibrary.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videolibrary.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView1 = findViewById(R.id.video1);

        VideoView videoView2 = findViewById(R.id.video2);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        String path2 = "android.resource://" + getPackageName() + "/" + R.raw.video2;
        videoView1.setVideoURI(Uri.parse(path));
        videoView1.requestFocus();
        videoView1.seekTo(1);

        videoView2.setVideoURI(Uri.parse(path2));
        videoView2.requestFocus();
        videoView2.seekTo(1);
        videoView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on video 1 clicked");
                Intent intent  = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra("video_path", path.toString());
                startActivity(intent);
            }
        });
        videoView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on video 2 clicked");
                Intent intent  = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra("video_path", path2.toString());
                startActivity(intent);
            }
        });
    }

}
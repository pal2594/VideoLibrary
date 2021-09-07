package com.example.videolibrary.ui;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videolibrary.Model.Video;
import com.example.videolibrary.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class VideoAdapter extends FirebaseRecyclerAdapter<Video, VideoAdapter.MyViewHolder> {

    Uri videoUri;
    MediaMetadataRetriever mediaMetadataRetriever;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VideoAdapter(@NonNull FirebaseRecyclerOptions<Video> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoAdapter.MyViewHolder myViewHolder, int i, @NonNull Video video) {
        myViewHolder.videoName.setText(video.getVideo_name());
        Log.d("STRING", video.getVideo_url());
        videoUri= Uri.parse(video.getVideo_url());
        Log.d("STRINGURL", videoUri.toString());
        myViewHolder.videoView.setVideoURI(videoUri);
        myViewHolder.videoView.seekTo(1);
    }

    @NonNull
    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_view,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView videoName;
        VideoView videoView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName=itemView.findViewById(R.id.text_videoName);
            videoView=itemView.findViewById(R.id.videoView);
        }
    }
}

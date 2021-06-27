package com.e.videoplayer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;

    static ArrayList<VideoFiles> mediaObjects;
    private RequestManager requestManager;
    static ArrayList<VideoFiles> videoFilesFull;



    public VideoPlayerRecyclerAdapter(Context mContext,ArrayList<VideoFiles> mediaObjects, RequestManager requestManager) {
        this.mContext = mContext;
        VideoPlayerRecyclerAdapter.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
        videoFilesFull=new ArrayList<>(mediaObjects);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoPlayerViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_video_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }



}















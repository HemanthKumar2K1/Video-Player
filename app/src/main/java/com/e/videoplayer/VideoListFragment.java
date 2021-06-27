package com.e.videoplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;

import static com.e.videoplayer.MainActivity.videoFiles;


public class VideoListFragment extends Fragment {


    private VideoPlayerRecyclerView mRecyclerView;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @SuppressLint("StaticFieldLeak")
    static VideoPlayerRecyclerAdapter videoPlayerRecyclerAdapter;
    View view;
    private SimpleExoPlayer videoPlayer;


    public VideoListFragment() {
        // Required empty public constructor


    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_main2, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);

        mRecyclerView.setMediaObjects(videoFiles);
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(getContext(), videoFiles, initGlide());
        mRecyclerView.setAdapter(adapter);

//        initRecyclerView();


        return view;
    }


    private void initRecyclerView() {

    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }



    @Override
    public void onDestroy() {
        if (mRecyclerView != null) {
            mRecyclerView.releasePlayer();

        }


        super.onDestroy();
    }

    @Override
    public void onPause() {
            mRecyclerView.pause();


        super.onPause();
    }

    @Override
    public void onResume() {
            mRecyclerView.resume();


        super.onResume();
    }
}
package com.e.videoplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Objects;

import static com.e.videoplayer.VideoAdapter.videoFiles;
import static com.e.videoplayer.VideoFolderAdapter.folderVideoFiles;


public class PlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    int position = -1, clickCount = 0, rotate = 0;
    ArrayList<VideoFiles> myFiles = new ArrayList<>();
    String path;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    ImageView lock, repeating, unlock, next_video, prev_video, ratio, back;
    LinearLayout unlock_panel;
    RelativeLayout root;
    private SeekBar seekBar;
    private int brightness;
    private ContentResolver cResolver;
    private Window window;
    boolean state = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenMethod();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_player);
        init();


        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        assert sender != null;
        if (sender.equals("FolderIsSending")) {
            myFiles = folderVideoFiles;
        } else {
            myFiles = videoFiles;
        }
        try {
            path = myFiles.get(position).getPath();
            if (path != null) {
                Uri uri = Uri.parse(path);
                simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();

                DataSource.Factory factory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "My App Name"));
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);


                playerView.setPlayer(simpleExoPlayer);
                playerView.setKeepScreenOn(true);
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);


               /* DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                        this, Util.getUserAgent(this, "My App Name"));
                String mediaUrl = myFiles.get(position).getPath();
                if (mediaUrl != null) {
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(mediaUrl));
                    simpleExoPlayer.prepare(videoSource);
                    simpleExoPlayer.setPlayWhenReady(true);


                }*/


            }
        } catch (Exception ignored) {



        }


//        volumeSeekbar.setThumbOffset(10000);
        volumeControls();


        cResolver = getContentResolver();
        window = getWindow();
        seekBar.setMax(255);
        seekBar.setKeyProgressIncrement(1);
        try {
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        brightness = 10;
        seekBar.setProgress(brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Context context = getApplicationContext();

                boolean settingsCanWrite = Settings.System.canWrite(context);

                if (!settingsCanWrite) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    startActivity(intent);
                }
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                layoutpars.screenBrightness = brightness / (float) 255;
                window.setAttributes(layoutpars);
                brightness = Math.max(progress, 20);

            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());


        lock.setOnClickListener(view -> {
            root.setVisibility(View.GONE);
            root.setEnabled(false);
            unlock_panel.setVisibility(View.VISIBLE);
            unlock_panel.setEnabled(true);

        });
        unlock.setOnClickListener(view -> {

            root.setVisibility(View.VISIBLE);
            root.setEnabled(true);
            unlock_panel.setVisibility(View.GONE);
            unlock_panel.setEnabled(false);


        });


        checkButton();


        next_video.setOnClickListener(view -> {
            position++;


            checkButton();

            if (position < myFiles.size()) {
                path = myFiles.get(position).getPath();

                if (path != null) {

                    Uri uri = Uri.parse(path);
                    DataSource.Factory factory = new DefaultDataSourceFactory(PlayerActivity.this, Util.getUserAgent(PlayerActivity.this, "My App Name"));
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);


                    playerView.setPlayer(simpleExoPlayer);
                    playerView.setKeepScreenOn(true);
                    simpleExoPlayer.prepare(mediaSource);
                    simpleExoPlayer.setPlayWhenReady(true);


                }
            }


        });
        prev_video.setOnClickListener(view -> {
            position--;

            checkButton();

            if (position >= 0) {
                path = myFiles.get(position).getPath();

                if (path != null) {

                    Uri uri = Uri.parse(path);
                    DataSource.Factory factory = new DefaultDataSourceFactory(PlayerActivity.this, Util.getUserAgent(PlayerActivity.this, "My App Name"));
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);
                    playerView.setPlayer(simpleExoPlayer);
                    playerView.setKeepScreenOn(true);
                    simpleExoPlayer.prepare(mediaSource);
                    simpleExoPlayer.setPlayWhenReady(true);


                }
            }

        });

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == Player.STATE_ENDED)
                    state = true;
            }
        });

    }

    private void init() {
        playerView = findViewById(R.id.exoplayer_movie);
        ratio = findViewById(R.id.ratio);
        lock = findViewById(R.id.lock);
        unlock = findViewById(R.id.unlock);
        unlock_panel = findViewById(R.id.unlock_panel);
        root = findViewById(R.id.rootLayout);
        next_video = findViewById(R.id.next_video);
        prev_video = findViewById(R.id.prev_video);
        seekBar = findViewById(R.id.brightness);
        volumeSeekbar = findViewById(R.id.volume);
        //repeating = findViewById(R.id.repeating);

    }

    //    @Override
//    public void onPlayerError(ExoPlaybackException error) {
//        switch (error.type) {
//            case ExoPlaybackException.TYPE_SOURCE:
//                Log.e("TAG", "TYPE_SOURCE: " + error.getSourceException().getMessage());
//                break;
//
//            case ExoPlaybackException.TYPE_RENDERER:
//                Log.e("TAG", "TYPE_RENDERER: " + error.getRendererException().getMessage());
//                break;
//
//            case ExoPlaybackException.TYPE_UNEXPECTED:
//                Log.e("TAG", "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
//                break;
//        }
//    }
    private void checkButton() {

        try {


            if (position == 0) {

                next_video.setEnabled(true);
                prev_video.setEnabled(false);
            } else if (position == myFiles.size() - 1) {
                next_video.setEnabled(false);
                prev_video.setEnabled(true);
            } else {
                next_video.setEnabled(true);
                prev_video.setEnabled(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void volumeControls() {
        try {


            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (simpleExoPlayer.isPlaying()) {
            simpleExoPlayer.setPlayWhenReady(false);


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (simpleExoPlayer.isPlaying()) {
            simpleExoPlayer.setPlayWhenReady(false);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.release();
    }

    private void setFullScreenMethod() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    public void ratio(View v) {
        if (clickCount == 0) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            ratio.setImageResource(R.drawable.ic_fit_to_screen);

            clickCount = 1;
        } else if (clickCount == 1) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            ratio.setImageResource(R.drawable.ic_fittoscreen);

            clickCount = 2;
        } else if (clickCount == 2) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            ratio.setImageResource(R.drawable.ic_fullscreen);

            clickCount = 0;
        }


    }

    public void rotation(View v) {


        if (rotate == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


            rotate = 0;
        } else if (rotate == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            rotate = 1;
        }


    }


}
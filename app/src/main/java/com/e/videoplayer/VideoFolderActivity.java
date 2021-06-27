package com.e.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;*/

import java.util.ArrayList;
import java.util.Objects;

public class VideoFolderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    VideoFolderAdapter videoFolderAdapter;
    String myFolderName;
    ArrayList<VideoFiles> videoFilesArrayList = new ArrayList<>();
    private SearchView searchView = null;


   /* public AdView mAdView, m1AdView;
    private InterstitialAd mInterstitialAd;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("XO Player    ");
        getSupportActionBar().setLogo(R.drawable.ic_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);

       /* AdView adView = new AdView(this);


        AdSize adSize = new AdSize(100, 56);
        adView.setAdSize(adSize);

        adView.setAdUnitId("ca-app-pub-4550331422976080/4237659458");

// TODO: Add adView to your view hierarchy.

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setVisibility(View.VISIBLE);

        mAdView.loadAd(adRequest);

        m1AdView = findViewById(R.id.adView1);
        m1AdView.loadAd(adRequest);
*/


        recyclerView = findViewById(R.id.FolderVideoRV);
        myFolderName = getIntent().getStringExtra("folderName");

        if (myFolderName != null) {
            videoFilesArrayList = getAllVideos(this, myFolderName);
        }
        if (videoFilesArrayList.size() > 0) {
            videoFolderAdapter = new VideoFolderAdapter(this, videoFilesArrayList);
            recyclerView.setAdapter(videoFolderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }



    }

    public ArrayList<VideoFiles> getAllVideos(Context context, String folderName) {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME


        };
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);
                String bucket_name = cursor.getString(7);

                VideoFiles videoFiles = new VideoFiles(id, path, title, fileName, size, dateAdded, duration);
                if (folderName.endsWith(bucket_name))
                    tempVideoFiles.add(videoFiles);


            }
            cursor.close();

        }
        return tempVideoFiles;
    }



    @Override
    public boolean onQueryTextSubmit(String query) {


        return false;
    }
      boolean isPause = false;





    @Override
    public boolean onQueryTextChange(String newText) {

        videoFolderAdapter.getFilter().filter(newText);
        //Toast.makeText(this,"SEARCH CLICKED", Toast.LENGTH_SHORT).show();

        /*if (isPause) {
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);
            isPause = false;
        }*/
        //if(!isPause) {
         //   mAdView.destroy();
           // mAdView.setVisibility(View.GONE);
           // isPause = true;
        //}


        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);


        MenuItem menuItem = menu.findItem(R.id.search_option);

        SearchView searchView = (SearchView) menuItem.getActionView();


        searchView.setOnQueryTextListener(this);
        //final AdRequest adRequest = new AdRequest.Builder().build();

        searchView.setOnCloseListener(() -> {
            //Toast.makeText(this,"SEARCH closed", Toast.LENGTH_SHORT).show();
           // mAdView.loadAd(adRequest);
            //mAdView.setVisibility(View.VISIBLE);


            return false;
        });



        return super.onCreateOptionsMenu(menu);
    }





//
//
//    int i = 0;
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
////            case R.id.layouts:
////                if (i == 0) {
////
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.mainFragment, new GridFolderFragment());
////                    fragmentTransaction.commit();
////                    i = 1;
////                } else if (i == 1) {
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
////                    fragmentTransaction.commit();
////                    i = 0;
////                }
////                break;
//            case R.id.search:
//                Toast.makeText(this,"Search Clicked",Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//
//
//    }

}
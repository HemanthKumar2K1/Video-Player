package com.e.videoplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.exoplayer2.SimpleExoPlayer;
/*import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;*/
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 123;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    BottomNavigationView bottomView;
    static ArrayList<VideoFiles> videoFiles = new ArrayList<>();
    static ArrayList<String> folderList = new ArrayList<>();
    FragmentTransaction fragmentTransaction;
    private SimpleExoPlayer videoPlayer;


    private final int share_item_id = 101;
    private boolean isItemAdd = false;

    MenuItem menuItem;
    private int i, index;
    private int k;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NUMBER = "number";

   /* public AdView mAdView, m1AdView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;*/


    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        videoPlayer=findViewById(R.id.videoList);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("XO Player          ");
        getSupportActionBar().setLogo(R.drawable.ic_icon);


      /* AdView adView = new AdView(this);
        AdSize adSize = new AdSize(100, 56);
        adView.setAdSize(adSize);
        adView.setAdUnitId("ca-app-pub-4550331422976080/4237659458");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                createpersonalisedAd();
                loadRewardedAd();
            }

        });


        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        m1AdView = findViewById(R.id.adView1);
        m1AdView.loadAd(adRequest);


 */
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        bottomView = findViewById(R.id.bottomNAvView);
        checkAndRequestPermissions();
        bottomView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.folderList:
                    //showRewardedAd();

                  /*  if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }*/
                    isItemAdd = false;

                    invalidateOptionsMenu();
//                    Toast.makeText(MainActivity.this, "Folder", Toast.LENGTH_SHORT).show();
                    if (index == 0) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                        fragmentTransaction.commit();
                    } else if (index == 1) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, new GridFolderFragment());
                        fragmentTransaction.commit();
                    }
                    item.setChecked(true);
                    break;
                case R.id.filesList:
                    //loadData();
                    //updateViews();

                    // showRewardedAd();

/*
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
*/

                    isItemAdd = true;
                    invalidateOptionsMenu();

//                    Toast.makeText(MainActivity.this, "Files", Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.mainFragment, new FilesFragment());
                    fragmentTransaction2.commit();
                    item.setChecked(true);
                    break;

                case R.id.filesPlayablelist:
                    //loadData();
                    //updateViews();

                    //showRewardedAd();


                    /*if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }*/

                    isItemAdd = true;
                    invalidateOptionsMenu();

//                    Toast.makeText(MainActivity.this, "Files", Toast.LENGTH_SHORT).show();
                    Collections.reverse(videoFiles);
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction3.replace(R.id.mainFragment, new VideoListFragment());


                    fragmentTransaction3.replace(R.id.mainFragment, new VideoListFragment());


                    fragmentTransaction3.commit();
                    item.setChecked(true);

                    break;


            }
            return false;
        });


    }



  /*  private void createpersonalisedAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitalAd(adRequest);

    }

    private void createInterstitalAd(AdRequest adRequest) {

        InterstitialAd.load(this, "ca-app-pub-4550331422976080/5359169433", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
//                m1InterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");
                createpersonalisedAd();


                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        //createpersonalisedAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("TAG", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });


    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-4550331422976080/6480679411",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d("TAG", "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("TAG", "Ad was shown.");
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d("TAG", "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                Log.d("TAG", "Ad was dismissed.");
                                loadRewardedAd();
                            }
                        });
                    }
                });
    }

    private void showRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("TAG", "The user earned the reward.");
                    //int rewardAmount = rewardItem.getAmount();
                    //String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }
    }

   */


    public void checkAndRequestPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        } else {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
         /*   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
            fragmentTransaction.commit();*/




              /*  if (i == 0) {

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainFragment, new GridFolderFragment());
                    fragmentTransaction.commit();
                } else if (i == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                    fragmentTransaction.commit();
                }*/
            loadData();
            updateViews();
            Log.d("TAG", "check and perm load data , updateviews");


            videoFiles = getAllVideos(this);


        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
                            .toArray(new String[1]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
               /* FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                fragmentTransaction.commit();*/




/*
                    if (i == 0) {

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, new GridFolderFragment());
                        fragmentTransaction.commit();
                    } else if (i == 1) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                        fragmentTransaction.commit();
                    }
*/
                //loadData();
                updateViews();
                Log.d("TAG", "on request load data , updateviews");


                videoFiles = getAllVideos(this);


            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

            }
        }
    }

    public ArrayList<VideoFiles> getAllVideos(Context context) {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME


        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);

                VideoFiles videoFiles = new VideoFiles(id, path, title, fileName, size, dateAdded, duration);
                Log.e("Path", path);


                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);


//                int index = subString.lastIndexOf("/");
//                String folderName = subString.substring(index + 1, slashFirstIndex);


                if (!folderList.contains(subString))
                    folderList.add(subString);
                tempVideoFiles.add(videoFiles);


            }
            cursor.close();

        }
        return tempVideoFiles;
    }


    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isItemAdd) {
            if (menu.findItem(share_item_id) == null) {
                menuItem = menu.add(Menu.NONE, share_item_id, 5, "layouts");
                menuItem.setIcon(R.drawable.ic_layout);

                menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);


                menuItem.setOnMenuItemClickListener(menuItem1 -> {


                    if (index == 0) {

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, new GridFolderFragment());
                        fragmentTransaction.commit();


                        index = 1;
                    } else if (index == 1) {

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                        fragmentTransaction.commit();

                        index = 0;
                    }
                    saveData();

                    return false;
                });


            }
        }

        return super.onPrepareOptionsMenu(menu);


    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NUMBER, String.valueOf(index));
        editor.apply();
        //  Toast.makeText(this, "Data saved "+index, Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        index = Integer.parseInt(sharedPreferences.getString(NUMBER, "0"));

    }

    public void updateViews() {
/*
            i=k;
*/
        if (index == 1) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, new GridFolderFragment());
            fragmentTransaction.commit();
        } else if (index == 0) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
            fragmentTransaction.commit();
        }

        //Toast.makeText(this, "updated "+index, Toast.LENGTH_SHORT).show();

    }

    //        else {
//            menu.removeItem(share_item_id);
//
//
//        }
 /*   boolean isPause = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //final AdRequest adRequest = new AdRequest.Builder().build();

        //MenuItem menuItem = menu.findItem(R.id.search_option);

       /* menuItem.setOnMenuItemClickListener(menuItem1 -> {
Toast.makeText(this,"SEARCH CLICKED",Toast.LENGTH_SHORT).show();

            /*if (isPause) {
                mAdView.loadAd(adRequest);
                mAdView.setVisibility(View.VISIBLE);
                isPause = false;
            } else {
                mAdView.destroy();
                mAdView.setVisibility(View.GONE);
                isPause = true;
            }
            return false;
        });

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this,"SEARCH CLICKED",Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        return true;





        /*SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(MainActivity.this);


        return super.onCreateOptionsMenu(menu);
    }

    /*public void search(MenuItem item) {
        Toast.makeText(this,"SEARCH CLICKED",Toast.LENGTH_SHORT).show();


    }

   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final AdRequest adRequest = new AdRequest.Builder().build();

        switch (item.getItemId()) {
            case R.id.search_option:
                Toast.makeText(this,"SEARCH CLICKED",Toast.LENGTH_SHORT).show();


                if (isPause) {
                mAdView.loadAd(adRequest);
                mAdView.setVisibility(View.VISIBLE);
                isPause = false;
            } else {
                mAdView.destroy();
                mAdView.setVisibility(View.GONE);
                isPause = true;
            }



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}





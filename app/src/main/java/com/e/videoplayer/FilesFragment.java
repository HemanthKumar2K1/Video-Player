package com.e.videoplayer;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.e.videoplayer.VideoAdapter;
import com.e.videoplayer.VideoFiles;
import com.e.videoplayer.R;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import static com.e.videoplayer.MainActivity.videoFiles;


public class FilesFragment extends Fragment {



    RecyclerView recyclerView;
    View view;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @SuppressLint("StaticFieldLeak")
    static VideoAdapter videoAdapter;
    boolean flag=true;
   //private AdView mAdView, m1AdView;


    public FilesFragment() {
        // Required empty public constructor

    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_files, container, false);




        recyclerView=view.findViewById(R.id.filesRV);
        if(videoFiles != null && videoFiles.size()>0){
            videoAdapter = new VideoAdapter(getContext(),videoFiles);
            recyclerView.setAdapter(videoAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        }

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_option);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


      //  mAdView = getActivity().findViewById(R.id.adView);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {



                    //flag= true;

                    Log.i("onQueryTextChange", newText);
//                    String text = newText;
//                    String userInput = newText.toLowerCase();
//        ArrayList<VideoFiles> myFiles = new ArrayList<>();
//        for( VideoFiles video: videoFiles){
//            if(video.getTitle().toLowerCase().contains(userInput)){
//                myFiles.add(video);
//            }
//        }
//        videoAdapter.updateList(videoFiles);
                    videoAdapter.getFilter().filter(newText);
                   // mAdView.destroy();
                    //mAdView.setVisibility(View.GONE);

                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);


                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

       // final AdRequest adRequest = new AdRequest.Builder().build();

        searchView.setOnCloseListener(() -> {
            //Toast.makeText(this,"SEARCH closed", Toast.LENGTH_SHORT).show();
          //  mAdView.loadAd(adRequest);
            //mAdView.setVisibility(View.VISIBLE);


            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_option:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
}
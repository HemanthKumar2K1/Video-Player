package com.e.videoplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.videoplayer.FolderAdapter;
import com.e.videoplayer.R;

import static com.e.videoplayer.MainActivity.folderList;
import static com.e.videoplayer.MainActivity.videoFiles;


public class FolderFragment extends Fragment {


    FolderAdapter folderAdapter;
    RecyclerView recyclerView;
    String myFolderName;

    public FolderFragment() {
        // Required empty public constructor
    }

public static final String TAG = "MyActivity";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_folder, container, false);


        recyclerView = view.findViewById(R.id.folderRV);

        if (folderList != null && folderList.size() > 0 && videoFiles != null) {
            folderAdapter = new FolderAdapter(folderList, videoFiles, getContext());


            recyclerView.setAdapter(folderAdapter);


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            Log.d("TAG", "Folder Linear");


//            if (myFolderName.equals("grid") ){
//                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false));
//
//            }
        }


        return view;

    }


}

package com.e.videoplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.videoplayer.GridFolderAdapter;
import com.e.videoplayer.R;

import static com.e.videoplayer.MainActivity.folderList;
import static com.e.videoplayer.MainActivity.videoFiles;


public class GridFolderFragment extends Fragment {


    GridFolderAdapter GridfolderAdapter;
    RecyclerView recyclerView;
    String myFolderName;

    public GridFolderFragment() {
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
            GridfolderAdapter = new GridFolderAdapter(folderList, videoFiles, getContext());


            recyclerView.setAdapter(GridfolderAdapter);


//            if (myFolderName.equals("grid") ){
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false));
            Log.d("TAG", "Folder Grid");

//
//            }
        }


        return view;

    }


}

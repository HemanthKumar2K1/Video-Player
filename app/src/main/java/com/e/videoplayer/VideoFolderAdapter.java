package com.e.videoplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> implements Filterable {
    private final Context mContext;
    static ArrayList<VideoFiles> folderVideoFiles;
    static ArrayList<VideoFiles> videoFilesFull;

    View view;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.fileName.setText(folderVideoFiles.get(position).getTitle());
        Glide.with(mContext).load(new File(folderVideoFiles.get(position).getPath())).into(holder.thumbnail);
        holder.videoDuration.setText(setTimer(Integer.parseInt(folderVideoFiles.get(position).getDuration())));
        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("sender", "FolderIsSending");
            mContext.startActivity(intent);
        });
        holder.menuMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, holder.menuMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_pop, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.delete) {
                    deleteFile(position, view);
                }
                if (menuItem.getItemId()== R.id.share){
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
/*
                    String sharebody="Your body here";
*/
                    String sharesub="Your subject here";
                    myIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                    myIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext,BuildConfig.APPLICATION_ID +".provider",new File(folderVideoFiles.get(position).getPath())));
                    myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mContext.startActivity(Intent.createChooser(myIntent,"Share  using"));
                }
                return true;
            });
            popupMenu.show();
        });


    }

    public void deleteFile(int position, View v) {

        Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Long.parseLong(folderVideoFiles.get(position).getId()));

        try {

            File file = new File(folderVideoFiles.get(position).getPath());
            boolean deleted = file.delete();
            if (deleted) {
                mContext.getContentResolver().delete(uri, null, null);
                folderVideoFiles.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, folderVideoFiles.size());
                Snackbar.make(v, "File Deleted", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(v, "Not Deleted", Snackbar.LENGTH_LONG).show();


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return folderVideoFiles.size();
    }

    public String setTimer(int time) {
        String str = "00:00";
        try {
            if ((long) time >= 3600000) {
                try {
                    str = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time), TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
                } catch (NumberFormatException unused) {
                    java.lang.System.out.println((long) time);
                }
            } else {
                str = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
            }
            return str;
        } catch (Exception e) {
            return "00:00";
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, menuMore;
        TextView fileName, videoDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            menuMore = itemView.findViewById(R.id.menu_more);
            fileName = itemView.findViewById(R.id.video_file_name);
            videoDuration = itemView.findViewById(R.id.video_duration);


        }
    }

    public VideoFolderAdapter(Context mContext, ArrayList<VideoFiles> folderVideoFiles) {
        this.mContext = mContext;
        VideoFolderAdapter.folderVideoFiles = folderVideoFiles;
        videoFilesFull=new ArrayList<>(folderVideoFiles);

    }
    public Filter getFilter() {
        return exampleFilter1;
    }
    private Filter exampleFilter1 = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VideoFiles> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(videoFilesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (VideoFiles item : videoFilesFull) {
                    if (item.getFileName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            folderVideoFiles.clear();
            folderVideoFiles.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}

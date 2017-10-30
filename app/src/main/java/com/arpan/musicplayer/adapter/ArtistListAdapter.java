package com.arpan.musicplayer.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpan.musicplayer.R;
import com.arpan.musicplayer.model.Artist;

import java.util.ArrayList;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Artist> mArtistsList;

    public ArtistListAdapter(Context context, ArrayList<Artist> artistsList) {
        mContext = context;
        mArtistsList = artistsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.artist_list_item, parent);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return mArtistsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mArtistName;
        public ImageView mArtistImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mArtistName = itemView.findViewById(R.id.artistNameInList);
            mArtistImage = itemView.findViewById(R.id.artistImageView);
        }

        public void bindViews(int position) {

            //TODO DO A LAST.FM API REQ. AND GET ARTIST IMAGE
            mArtistName.setText(mArtistsList.get(position).getArtistName());
        }
    }
}

package com.example.nb.androidfinalproject.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.R;

import java.util.ArrayList;
import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder>{

    private Context adapterContext;
    private List<Movie> movies;
    private WatchListAdapter.OnItemClickListener listener;

    public WatchListAdapter(ArrayList<Movie> moviesList, Context context){
        movies = moviesList;
        adapterContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView movieImageView;
        private TextView titleTv;

        public ViewHolder(View itemView, final WatchListAdapter.OnItemClickListener listener) {

            super(itemView);
            movieImageView = itemView.findViewById(R.id.movie_image_iv);
            titleTv = itemView.findViewById(R.id.title_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public WatchListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_movie_card_item, viewGroup, false);
        WatchListAdapter.ViewHolder vh = new WatchListAdapter.ViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Movie currentItem = movies.get(i);

        Glide.with(adapterContext).load("https://image.tmdb.org/t/p/w500" + movies.get(i).getPosterPath()).into(viewHolder.movieImageView);

        viewHolder.titleTv.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(WatchListAdapter.OnItemClickListener clickListener){
        listener = clickListener;
    }
}

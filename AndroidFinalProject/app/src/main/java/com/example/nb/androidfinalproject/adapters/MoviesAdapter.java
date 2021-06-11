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

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context adapterContext;
    private List<Movie> movies;
    private OnItemClickListener listener;

    public MoviesAdapter(ArrayList<Movie> moviesList, Context context){
        movies = moviesList;
        adapterContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView movieImageView;
        private TextView titleTv;
        private TextView releaseDateTv;

        public ViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);
            movieImageView = itemView.findViewById(R.id.movie_image_iv);
            titleTv = itemView.findViewById(R.id.title_tv);
            releaseDateTv = itemView.findViewById(R.id.release_date_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(movieImageView, position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Movie currentItem = movies.get(i);

        Glide.with(adapterContext)
                .load("https://image.tmdb.org/t/p/w500" + movies.get(i).getPosterPath())
                .into(viewHolder.movieImageView);

        viewHolder.titleTv.setText(currentItem.getTitle());
        viewHolder.releaseDateTv.setText(currentItem.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public void updateData(List<Movie> movies) {

        this.movies.clear();
        this.movies.addAll(movies);

    }



}

package com.example.nb.androidfinalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nb.androidfinalproject.DataBase.MovieDBRepository;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> implements NextPageCallBack {

    public static final int PAGE_SIZE = 20;
    public static final int OFFSET = 5;

    private Context adapterContext;
    private List<Movie> movies;
    private GenreListAdapter.OnItemClickListener listener;


    private OnNextPageRequired pager;

    public GenreListAdapter(List<Movie> moviesList, Context context) {
        movies = moviesList;
        adapterContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView movieImageView;
        private TextView titleTv;
        private TextView dateTv;

        public ViewHolder(View itemView, final GenreListAdapter.OnItemClickListener listener) {

            super(itemView);
            movieImageView = itemView.findViewById(R.id.viewpager_movie_image_iv);
            titleTv = itemView.findViewById(R.id.viewpager_title_tv);
            dateTv = itemView.findViewById(R.id.viewpager_release_date_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(movieImageView, position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public GenreListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_viewpager_card_item, viewGroup, false);
        GenreListAdapter.ViewHolder vh = new GenreListAdapter.ViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Movie currentItem = movies.get(i);

        Glide.with(adapterContext).load("https://image.tmdb.org/t/p/w500" + movies.get(i).getPosterPath()).into(viewHolder.movieImageView);

        viewHolder.titleTv.setText(currentItem.getTitle());
        viewHolder.dateTv.setText(currentItem.getReleaseDate());

        if (i == PAGE_SIZE - OFFSET) {
            if (pager != null) {
                pager.getNextPage(movies.size() / PAGE_SIZE, this);
            }
        }
    }

    @Override
    public void getNextPage(List<Movie> nextPage) {
        movies.addAll(nextPage);
        notifyItemRangeInserted(movies.size() - PAGE_SIZE, PAGE_SIZE);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(GenreListAdapter.OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public void updateData(List<Movie> movies) {

        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();

    }

    public void setPager(OnNextPageRequired pager) {
        this.pager = pager;
    }

    public interface OnNextPageRequired {
        void getNextPage(int pageNumber, NextPageCallBack calBack);
    }

}


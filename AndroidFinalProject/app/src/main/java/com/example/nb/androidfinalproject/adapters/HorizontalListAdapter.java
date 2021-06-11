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

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ViewHolder>{

    private Context adapterContext;
    private List<Movie> movies;
    private HorizontalListAdapter.OnItemClickListener listener;

    public HorizontalListAdapter(List<Movie> moviesList, Context context){
        movies = moviesList;
        adapterContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView movieImageView;
        private TextView titleTv;

        public ViewHolder(View itemView, final HorizontalListAdapter.OnItemClickListener listener) {

            super(itemView);
            movieImageView = itemView.findViewById(R.id.movie_image_iv);
            titleTv = itemView.findViewById(R.id.title_tv);

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
    public HorizontalListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_movie_card_item, viewGroup, false);
        HorizontalListAdapter.ViewHolder vh = new HorizontalListAdapter.ViewHolder(v, listener);
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
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(HorizontalListAdapter.OnItemClickListener clickListener){
        listener = clickListener;
    }

    public void updateData(List<Movie> movies) {

        this.movies = new ArrayList<>();
        this.movies.addAll(movies);
        notifyDataSetChanged();

    }


}

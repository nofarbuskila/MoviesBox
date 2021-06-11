package com.example.nb.androidfinalproject.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nb.androidfinalproject.DataBase.UserData;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.DataModels.MovieDetails;
import com.example.nb.androidfinalproject.DataModels.MovieReview;
import com.example.nb.androidfinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context adapterContext;
    private MovieDetails movieDetails;
    private ArrayList detailsList;

    public MovieDetailsAdapter(Context adapterContext, MovieDetails movieDetails) {
        this.adapterContext = adapterContext;
        this.movieDetails = movieDetails;
        this.detailsList = joinToOneList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        if(type == 1) {
            return new ViewHolderMovieDetails(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_details_row, viewGroup, false));
        }else{
            return new ViewHolderReview(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof ViewHolderMovieDetails) {
            ((ViewHolderMovieDetails)viewHolder).onBind(movieDetails.getMovie());
        }else {
            ((ViewHolderReview)viewHolder).onBind((MovieReview)detailsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 2;
    }

    public ArrayList joinToOneList(){

        ArrayList newList = new ArrayList();

        newList.add(movieDetails.getMovie());

        if(movieDetails.getReviews() != null){
            for (int i = 0; i < movieDetails.getReviews().size(); i++) {
                newList.add(movieDetails.getReviews().get(i));
            }
        }
        return newList;
    }

    public void addReview(MovieReview reviewToAdd){
        detailsList.add(1,reviewToAdd);
        this.notifyItemInserted(1);
    }
}

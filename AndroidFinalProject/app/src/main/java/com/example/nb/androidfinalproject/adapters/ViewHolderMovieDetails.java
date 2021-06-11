package com.example.nb.androidfinalproject.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nb.androidfinalproject.DataBase.UserData;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMovieDetails extends RecyclerView.ViewHolder {

    private static final String WISH_LIST = "wishList";
    private static final String WATCH_LIST = "watchList";

    private FirebaseUser currentUser;
    private DatabaseReference myRef;

    private boolean inWishList = false;
    private boolean inWatchList = false;

    private String id;

    //movie details row
    private ImageView movieImageView;
    private TextView titleTv;
    private TextView releaseDateTv;
    private TextView moviePlotTv;
    private Button wishBtn, watchBtn;

    public ViewHolderMovieDetails(View itemView) {
        super(itemView);
    }

    public void onBind(Movie movie){

        id = movie.getId();

        movieImageView = itemView.findViewById(R.id.poster_iv);
        titleTv = itemView.findViewById(R.id.movie_title_tv);
        releaseDateTv = itemView.findViewById(R.id.movie_year_tv);
        moviePlotTv = itemView.findViewById(R.id.movie_plot_tv);

        Glide.with(itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(movieImageView);

        titleTv.setText(movie.getTitle());
        releaseDateTv.setText(movie.getReleaseDate());
        moviePlotTv.setText(movie.getOverview());

        if (UserData.getInstance().getUser().getWishList() == null)
            UserData.getInstance().getUser().setWishList(new ArrayList<String>());

        wishBtn = itemView.findViewById(R.id.add_to_wishlist_btn);
        wishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveItem(WISH_LIST, inWishList);
            }
        });

        setInWishList(UserData.getInstance().getUser().getWishList().contains(id));

        if (UserData.getInstance().getUser().getWatchList() == null)
            UserData.getInstance().getUser().setWatchList(new ArrayList<String>());

        watchBtn = itemView.findViewById(R.id.add_to_watchlist_btn);
        watchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveItem(WATCH_LIST, inWatchList);

            }
        });

        setInWatchList(UserData.getInstance().getUser().getWatchList().contains(id));
    }

    private void setInWatchList(boolean inWatchList) {
        this.inWatchList = inWatchList;
        if (!inWatchList) {
            watchBtn.setSelected(false);
        } else {
            watchBtn.setSelected(true);
        }
    }

    private void setInWishList(boolean inWishList) {
        this.inWishList = inWishList;
        if (!inWishList) {
            wishBtn.setSelected(false);
        } else {
            wishBtn.setSelected(true);
        }
    }

    private void addOrRemoveItem(final String listType, final boolean isAdd) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        List<String> list = listType.equals(WATCH_LIST) ? UserData.getInstance().getUser().getWatchList() : UserData.getInstance().getUser().getWishList();

        if (list == null)
            list = new ArrayList<>();

        if (!isAdd)
            list.add(id);
        else
            list.remove(id);

        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child(listType);

        myRef.setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listType.equals(WISH_LIST))
                        setInWishList(!inWishList);
                    else
                        setInWatchList(!inWatchList);
                }
            }
        });
    }
}


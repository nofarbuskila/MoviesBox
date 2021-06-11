package com.example.nb.androidfinalproject;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.DataModels.MovieDetails;
import com.example.nb.androidfinalproject.DataModels.MovieReview;
import com.example.nb.androidfinalproject.adapters.MovieDetailsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MovieDetailsFragment extends Fragment implements AddReviewDialogFragment.OnReviewAddListener {

    final String ADD_REVIEW_FRAGMENT_TAG = "add_review_fragment";
    final String MOVIES = "Movies";

    private Toolbar toolbar;

    private String poster, title, overview, releaseDate, rating, backdrop, id;

    private ImageView thumbnail_iv;
    private FloatingActionButton addReviewFab;
    RecyclerView recyclerView;


    //FIREBASE
    private FirebaseDatabase database;
    private DatabaseReference movieFireBaseReference;

    private MovieDetailsAdapter adapter;

    private FragmentManager fragmentManager;

    private View rootView;

    private MovieDetails movieDetails;
    private Movie movie;

    private Object syncObject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            poster = getArguments().getString("poster");
            title = getArguments().getString("title");
            overview = getArguments().getString("overview");
            releaseDate = getArguments().getString("release_date");
            rating = getArguments().getString("rating");
            backdrop = getArguments().getString("backdrop");
            id = getArguments().getString("id");

            movie = new Movie(id, title, overview, releaseDate, poster, backdrop, rating,null);
        }

        database = FirebaseDatabase.getInstance();
        fragmentManager = getFragmentManager();

        movieFireBaseReference = database.getReference(MOVIES).child(id);
        movieFireBaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieDetails =  dataSnapshot.getValue(MovieDetails.class);

                if (movieDetails == null)
                    movieDetails = new MovieDetails();

                movieDetails.setMovie(movie);
                movieFireBaseReference.setValue(movieDetails);

                buildRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        thumbnail_iv = rootView.findViewById(R.id.thumbnail_iv);
        recyclerView = rootView.findViewById(R.id.content_recycler);

        //buildRecycleView();

        Glide.with(getActivity())
                .load("https://image.tmdb.org/t/p/w500" + backdrop)
                .into(thumbnail_iv);

        addReviewFab = rootView.findViewById(R.id.fab_add_review);
        addReviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddReviewDialogFragment().setOnReviewAddListener(MovieDetailsFragment.this).show(fragmentManager, ADD_REVIEW_FRAGMENT_TAG);
            }
        });

        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (((HomeActivity) getActivity()).getSupportActionBar() != null) {

            ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        }

        return rootView;
    }

    private void buildRecycleView(){
        adapter = new MovieDetailsAdapter(getContext(), movieDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAddReview(MovieReview review) {
        if(movieDetails.getReviews() == null)
            movieDetails.setReviews(new ArrayList<MovieReview>());

        movieDetails.getReviews().add(review);
        movieFireBaseReference.setValue(movieDetails);
        adapter.addReview(review);
    }
}

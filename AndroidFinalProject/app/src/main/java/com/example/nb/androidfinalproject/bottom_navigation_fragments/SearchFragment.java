package com.example.nb.androidfinalproject.bottom_navigation_fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nb.androidfinalproject.DataBase.MovieDBRepository;
import com.example.nb.androidfinalproject.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.adapters.MoviesAdapter;
import com.example.nb.androidfinalproject.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    ArrayList<Movie> moviesList = new ArrayList<>();

    View view;

    RelativeLayout relativeLayout;

    ProgressBar progressBar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MoviesAdapter moviesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);

        relativeLayout = view.findViewById(R.id.search_relative_layout);

        buildRecycleView();

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty())
                    parseJson(getContext(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty())
                    parseJson(getContext(), newText);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void buildRecycleView(){

        recyclerView = view.findViewById(R.id.movies_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 3);
        moviesAdapter = new MoviesAdapter(moviesList, getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moviesAdapter);

        moviesAdapter.setOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Movie currMovie = moviesList.get(position);

                Bundle bundle = new Bundle();

                bundle.putString("poster", currMovie.getPosterPath());
                bundle.putString("title", currMovie.getTitle());
                bundle.putString("overview", currMovie.getOverview());
                bundle.putString("release_date", currMovie.getReleaseDate());
                bundle.putString("rating", currMovie.getVote_average());
                bundle.putString("backdrop", currMovie.getBackdropPath());
                bundle.putString("id", currMovie.getId());

                ((HomeActivity)getActivity()).goToMovieDetailsFragment(bundle,v);

            }
        });
    }

    private void parseJson(final Context context, String query){

       MovieDBRepository.getInstance().getMoviesByName(getContext(), query, new MovieDBRepository.ResponseCallBack() {
            @Override
            public void onSuccess(List<Movie> movies) {
                moviesList.addAll(movies);
                moviesAdapter.updateData(movies);
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String Message) {
                Snackbar.make(relativeLayout, "Something went wrong... Please try again", Snackbar.LENGTH_LONG).show();

            }
        });
    }


}

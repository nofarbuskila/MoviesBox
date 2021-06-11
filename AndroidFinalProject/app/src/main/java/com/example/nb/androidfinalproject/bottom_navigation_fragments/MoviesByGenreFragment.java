package com.example.nb.androidfinalproject.bottom_navigation_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nb.androidfinalproject.DataBase.MovieDBRepository;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.HomeActivity;
import com.example.nb.androidfinalproject.R;
import com.example.nb.androidfinalproject.adapters.GenreListAdapter;
import com.example.nb.androidfinalproject.adapters.HorizontalListAdapter;
import com.example.nb.androidfinalproject.adapters.MoviesAdapter;
import com.example.nb.androidfinalproject.adapters.NextPageCallBack;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesByGenreFragment extends Fragment {

    private ArrayList<Movie> moviesByGenreList = new ArrayList();

    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    private View v;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GenreListAdapter moviesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_movies_by_genre, container, false);

        buildRecycleView();

        String genre = getArguments().getSerializable("genre").toString();

        parseJson((MovieDBRepository.eGenres)getArguments().getSerializable("genre"));

        TextView titleTv = v.findViewById(R.id.by_id_title);
        titleTv.setText(genre);

        return v;
    }

    private void buildRecycleView(){

        RecyclerView recycler = v.findViewById(R.id.genre_rv);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getContext(), LinearLayoutManager.HORIZONTAL, false);
        moviesAdapter = new GenreListAdapter(moviesByGenreList, getContext());
        moviesAdapter.setPager(new GenreListAdapter.OnNextPageRequired() {
            @Override
            public void getNextPage(int pageNumber, final NextPageCallBack calBack) {
                MovieDBRepository.getInstance().getMoviesByGenres(getContext(), MovieDBRepository.eGenres.ACTION, pageNumber, new MovieDBRepository.ResponseCallBack() {
                    @Override
                    public void onSuccess(List<Movie> movies) {
                        calBack.getNextPage(movies);
                    }

                    @Override
                    public void onFail(String Message) {

                    }
                });
            }
        });
        moviesAdapter.setOnItemClickListener(new GenreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Movie movie = moviesByGenreList.get(position);
                Bundle bundle = new Bundle();

                bundle.putString("poster", movie.getPosterPath());
                bundle.putString("title", movie.getTitle());
                bundle.putString("overview", movie.getOverview());
                bundle.putString("release_date", movie.getReleaseDate());
                bundle.putString("rating", movie.getVote_average());
                bundle.putString("backdrop", movie.getBackdropPath());
                bundle.putString("id", movie.getId());

                ((HomeActivity)getActivity()).goToMovieDetailsFragment(bundle, v);
            }
        });
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(moviesAdapter);
    }

    private void parseJson(MovieDBRepository.eGenres genres){

        MovieDBRepository.getInstance().getMoviesByGenres(getContext(),genres,1, new MovieDBRepository.ResponseCallBack() {
            @Override
            public void onSuccess(List<Movie> movies) {
                moviesByGenreList.addAll(movies);
                moviesAdapter.updateData(movies);
            }

            @Override
            public void onFail(String Message) {
                Snackbar.make(getView(), "Something went wrong... Please try again", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

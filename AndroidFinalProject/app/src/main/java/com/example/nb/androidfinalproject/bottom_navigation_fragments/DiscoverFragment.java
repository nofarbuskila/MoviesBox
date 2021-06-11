package com.example.nb.androidfinalproject.bottom_navigation_fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.nb.androidfinalproject.DataBase.MovieDBRepository;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.HomeActivity;
import com.example.nb.androidfinalproject.R;
import com.example.nb.androidfinalproject.adapters.HorizontalListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    private RelativeLayout relativeLayout;

    private View v;

    private ArrayList<Movie> upComingList = new ArrayList();
    private ArrayList<Movie> topRatedList = new ArrayList();
    private ArrayList<Movie> popularList = new ArrayList();
    private ArrayList<Movie> nowPlayingList = new ArrayList();

    private HorizontalListAdapter listAdapterUp;
    private HorizontalListAdapter listAdapterTop;
    private HorizontalListAdapter listAdapterPop;
    private HorizontalListAdapter listAdapterNow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_whats_hot, container, false);

        buildRecycleView();

        parseJson();

        return v;
    }

    private void buildRecycleView(){

        buildRecycleViewHelper();

        /*wishListAdapter.setOnItemClickListener(new WishListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Movie currMovie = moviesWishList.get(position);

                String poster = currMovie.getPosterPath();
                String title = currMovie.getTitle();
                String overview = currMovie.getOverview();
                String release_date = currMovie.getReleaseDate();
                String rating = currMovie.getVote_average();
                String backdrop = currMovie.getBackdropPath();

                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("poster", poster);
                intent.putExtra("title", title);
                intent.putExtra("overview", overview);
                intent.putExtra("release_date", release_date);
                intent.putExtra("rating", rating);
                intent.putExtra("backdrop", backdrop);
                startActivity(intent);
            }
        });*/
    }

    private void buildRecycleViewHelper(){

        RecyclerView recyclerViewUpComing = v.findViewById(R.id.upcoming_rv);
        RecyclerView recyclerViewTopRated = v.findViewById(R.id.top_rated_rv);
        RecyclerView recyclerViewPopular = v.findViewById(R.id.popular_rv);
        RecyclerView recyclerViewNowPlaying = v.findViewById(R.id.now_playing_rv);

        recyclerViewUpComing.setHasFixedSize(true);
        recyclerViewTopRated.setHasFixedSize(true);
        recyclerViewPopular.setHasFixedSize(true);
        recyclerViewNowPlaying.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager (getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager (getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager (getContext(), LinearLayoutManager.HORIZONTAL, false);

        listAdapterUp = new HorizontalListAdapter(upComingList, getContext());
        listAdapterUp.setOnItemClickListener(new HorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                openMovieFragment(v,upComingList.get(position));
            }
        });
        listAdapterTop = new HorizontalListAdapter(topRatedList, getContext());
        listAdapterTop.setOnItemClickListener(new HorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                openMovieFragment(v,topRatedList.get(position));
            }
        });
        listAdapterPop = new HorizontalListAdapter(popularList, getContext());
        listAdapterPop.setOnItemClickListener(new HorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                openMovieFragment(v,popularList.get(position));
            }
        });
        listAdapterNow = new HorizontalListAdapter(nowPlayingList, getContext());
        listAdapterNow.setOnItemClickListener(new HorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                openMovieFragment(v,nowPlayingList.get(position));
            }
        });

        recyclerViewUpComing.setLayoutManager(layoutManager);
        recyclerViewTopRated.setLayoutManager(layoutManager1);
        recyclerViewPopular.setLayoutManager(layoutManager2);
        recyclerViewNowPlaying.setLayoutManager(layoutManager3);

        recyclerViewUpComing.setAdapter(listAdapterUp);
        recyclerViewTopRated.setAdapter(listAdapterTop);
        recyclerViewPopular.setAdapter(listAdapterPop);
        recyclerViewNowPlaying.setAdapter(listAdapterNow);
    }

    private void parseJson(){
        parseJsonUpComing(getContext());
        parseJsonTopRated(getContext());
        parseJsonPopular(getContext());
        parseJsonNowPlaying(getContext());
    }

    private void parseJsonUpComing(final Context context){

        MovieDBRepository.getInstance().getUpcomingMovies(getContext(), new MovieDBRepository.ResponseCallBack() {
            @Override
            public void onSuccess(List<Movie> movies) {
                upComingList.addAll(movies);
                listAdapterUp.updateData(movies);
                listAdapterUp.notifyDataSetChanged();
            }

            @Override
            public void onFail(String Message) {
                Snackbar.make(getView(), "Something went wrong... Please try again", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void parseJsonTopRated(final Context context){

        MovieDBRepository.getInstance().getTopRatedMovies(getContext(), new MovieDBRepository.ResponseCallBack() {
            @Override
            public void onSuccess(List<Movie> movies) {
                topRatedList.addAll(movies);
                listAdapterTop.updateData(movies);
                listAdapterTop.notifyDataSetChanged();
            }

            @Override
            public void onFail(String Message) {
                Snackbar.make(getView(), "Something went wrong... Please try again", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void parseJsonPopular(final Context context){

        MovieDBRepository.getInstance().getPopularMovies(getContext(), new MovieDBRepository.ResponseCallBack() {
            @Override
            public void onSuccess(List<Movie> movies) {
                popularList.addAll(movies);
                listAdapterPop.updateData(movies);
                listAdapterPop.notifyDataSetChanged();
            }

            @Override
            public void onFail(String Message) {
                Snackbar.make(getView(), "Something went wrong... Please try again", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void parseJsonNowPlaying(final Context context){

        MovieDBRepository.getInstance().getNowPlayingMovies(getContext(), new MovieDBRepository.ResponseCallBack() {
            @Override
            public void onSuccess(List<Movie> movies) {
                nowPlayingList.addAll(movies);
                listAdapterNow.updateData(movies);
                listAdapterNow.notifyDataSetChanged();
            }

            @Override
            public void onFail(String Message) {
                Snackbar.make(getView(), "Something went wrong... Please try again", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void openMovieFragment(View v, Movie movie){
        Bundle bundle = new Bundle();

        bundle.putString("poster", movie.getPosterPath());
        bundle.putString("title", movie.getTitle());
        bundle.putString("overview", movie.getOverview());
        bundle.putString("release_date", movie.getReleaseDate());
        bundle.putString("rating", movie.getVote_average());
        bundle.putString("backdrop", movie.getBackdropPath());
        bundle.putString("id", movie.getId());

        ((HomeActivity)getActivity()).goToMovieDetailsFragment(bundle,v);

    }

}

package com.example.nb.androidfinalproject.DataBase;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nb.androidfinalproject.DataModels.Movie;

import java.io.Serializable;
import java.util.List;

//https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg

/*MOVIE*/
//sample to search movie:
//https://api.themoviedb.org/3/search/movie?api_key=fc72f643d82aa0179a21ff9f438297b5&language=en-US&query=alad&page=1&include_adult=false

//most popular - URL
//https://api.themoviedb.org/3/discover/movie?api_key=fc72f643d82aa0179a21ff9f438297b5&sort_by=popularity.desc&page=1&certification_country=IL
//https://api.themoviedb.org/3/movie/top_rated?api_key=fc72f643d82aa0179a21ff9f438297b5&language=en-US&page=1

//https://api.themoviedb.org/3/discover/movie?with_genres=18&api_key=fc72f643d82aa0179a21ff9f438297b5&popularity.desc


public class MovieDBRepository {
    private static final MovieDBRepository ourInstance = new MovieDBRepository();
    private static String API_KEY = "api_key=fc72f643d82aa0179a21ff9f438297b5"; //movies themoviedb
    private static String BASE_URL = "https://api.themoviedb.org/3/"; //movies themoviedb
    private static String SEARCH_URL = "https://api.themoviedb.org/3/search/movie?";//api_key=fc72f643d82aa0179a21ff9f438297b5&query=Aladdin";

    public static MovieDBRepository getInstance() {
        return ourInstance;
    }

    private MovieDBRepository() {
    }

    public void getUpcomingMovies(Context context, final ResponseCallBack callBack) {
        getMovies(context, callBack, "movie/upcoming?");
    }

    public void getTopRatedMovies(Context context, final ResponseCallBack callBack) {
        getMovies(context, callBack, "movie/top_rated?");
    }

    public void getPopularMovies(Context context, final ResponseCallBack callBack) {
        getMovies(context, callBack, "movie/popular?");
    }

    public void getNowPlayingMovies(Context context, final ResponseCallBack callBack) {
        getMovies(context, callBack, "movie/now_playing?");
    }

    public void getMoviesByGenres(Context context, eGenres genres, int pageNumber, final ResponseCallBack callBack) {

        RequestQueue queue = Volley.newRequestQueue(context);
        ResponseCallBackAdapter adapterCallback = new ResponseCallBackAdapter(callBack);
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "discover/movie?" + API_KEY + "&with_genres=" + genres.code + "&page=" + pageNumber + "&sort_by=popularity.desc", adapterCallback, adapterCallback);
        queue.add(request);
    }

    public void getMoviesByName(Context context, String name, final ResponseCallBack callBack) {
        RequestQueue queue = Volley.newRequestQueue(context);
        ResponseCallBackAdapter adapterCallback = new ResponseCallBackAdapter(callBack);
        StringRequest request = new StringRequest(Request.Method.GET, SEARCH_URL + API_KEY + "&query=" + name, adapterCallback, adapterCallback);
        queue.add(request);
    }

    public void getMoviesByID(Context context, String id, final ResponseCallBackById callBackById) {
        RequestQueue queue = Volley.newRequestQueue(context);
        ResponseCallBackByIdAdapter adapterCallback = new ResponseCallBackByIdAdapter(callBackById);
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "movie/" + id + "?" + API_KEY, adapterCallback, adapterCallback);
        queue.add(request);

    }

    private void getMovies(Context context, final ResponseCallBack callBack, String query) {
        RequestQueue queue = Volley.newRequestQueue(context);
        ResponseCallBackAdapter adapterCallback = new ResponseCallBackAdapter(callBack);
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + query + API_KEY, adapterCallback, adapterCallback);
        queue.add(request);

    }

    public interface ResponseCallBack {
        void onSuccess(List<Movie> movies);

        void onFail(String Message);
    }

    public interface ResponseCallBackById {
        void onSuccess(List<Movie> movies);

        void onFail(String Message);
    }

    public enum eGenres {

        ACTION(28),
        ADVENTURE(12),
        ANIMATION(16),
        COMEDY(35),
        CRIME(80),
        DOCUMENTARY(99),
        DRAMA(18),
        FAMILY(10751),
        FANTASY(14),
        HISTORY(36),
        HORROR(27),
        MUSIC(10402),
        MYSTERY(9648),
        ROMANCE(10749),
        SCIENCE_FICTION(878),
        WAR(10752),
        WESTERN(37);

        private int code;

        eGenres(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }


}

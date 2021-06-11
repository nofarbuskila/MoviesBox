package com.example.nb.androidfinalproject.DataBase;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.nb.androidfinalproject.DataModels.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResponseCallBackByIdAdapter implements Response.Listener<String> , Response.ErrorListener{

    private MovieDBRepository.ResponseCallBackById callBack;

    public ResponseCallBackByIdAdapter(MovieDBRepository.ResponseCallBackById callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onResponse(String response) {
        List<Movie> movies = parseResponse(response);
        callBack.onSuccess(movies);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        callBack.onFail(error.getMessage());

    }

    private List<Movie> parseResponse(String response) {

        List<Movie> moviesList = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(response);

            String id = rootObject.getString("id");
            String title = rootObject.getString("title");
            String overview = rootObject.getString("overview");
            String releaseDate = rootObject.getString("release_date");
            String poster = rootObject.getString("poster_path");
            String backdrop = rootObject.getString("backdrop_path");
            String rate = rootObject.getString("vote_average");
            JSONArray genresArray = rootObject.getJSONArray("genres");
            JSONObject genre;
            List genresList = new ArrayList();
            for (int i = 0; i < genresArray.length(); i++) {
                genre = genresArray.getJSONObject(i);
                genresList.add(genre.getString("name"));
            }

            moviesList.add(new Movie(id, title, overview, releaseDate, poster, backdrop, rate, genresList));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviesList;
    }
}
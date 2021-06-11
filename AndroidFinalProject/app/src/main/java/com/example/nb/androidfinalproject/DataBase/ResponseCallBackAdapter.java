package com.example.nb.androidfinalproject.DataBase;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.nb.androidfinalproject.DataModels.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResponseCallBackAdapter implements Response.Listener<String> , Response.ErrorListener{

    private MovieDBRepository.ResponseCallBack callBack;

    public ResponseCallBackAdapter(MovieDBRepository.ResponseCallBack callBack) {
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

        ArrayList<Movie> moviesList = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(response);

            JSONArray resultsArray = rootObject.getJSONArray("results");
            String totalResults = rootObject.getString("total_results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultsObject = resultsArray.getJSONObject(i);

                JSONArray genresArray = resultsObject.getJSONArray("genre_ids");
                ArrayList<String> genres = new ArrayList<>();
                if (genresArray != null)
                    for (int j = 0; j < genresArray.length(); j++)
                        genres.add(genresArray.getString(j));

                String id = resultsObject.getString("id");
                String title = resultsObject.getString("title");
                String overview = resultsObject.getString("overview");
                String releaseDate = resultsObject.getString("release_date");
                String poster = resultsObject.getString("poster_path");
                String backdrop = resultsObject.getString("backdrop_path");
                String rate = resultsObject.getString("vote_average");

                moviesList.add(moviesList.size(), new Movie(id, title, overview, releaseDate, poster, backdrop, rate, genres));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviesList;
    }
}
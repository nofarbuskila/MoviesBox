package com.example.nb.androidfinalproject.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.nb.androidfinalproject.DataModels.MovieReview;
import com.example.nb.androidfinalproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderReview extends RecyclerView.ViewHolder {

    //movie review
    private TextView rTitle_tv, rRate_tv, rUsername_tv, rDate_tv, rBody_tv;

    public ViewHolderReview(View itemView) {
        super(itemView);
    }

    public void onBind(MovieReview movieReview){
        rTitle_tv = itemView.findViewById(R.id.rev_title_tv);
        rRate_tv = itemView.findViewById(R.id.rev_rate_tv);
        rUsername_tv = itemView.findViewById(R.id.rev_username_tv);
        rDate_tv = itemView.findViewById(R.id.rev_date_tv);
        rBody_tv = itemView.findViewById(R.id.rev_body_tv);


        rTitle_tv.setText(movieReview.getTitle());
        rRate_tv.setText(movieReview.getRate());
        rUsername_tv.setText(movieReview.getUserName());
        rDate_tv.setText(parseDateToString(movieReview.getDate()));
        rBody_tv.setText(movieReview.getBody());
    }

    private String parseDateToString (Date date){
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);

        return dateAsString;
    }
}

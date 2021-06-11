package com.example.nb.androidfinalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nb.androidfinalproject.DataBase.UserData;
import com.example.nb.androidfinalproject.DataModels.MovieReview;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddReviewDialogFragment extends DialogFragment {


    View view;

    TextView titleTv;
    EditText reviewTitleEt, bodyEt, rateEt;
    Button addReviewBtn;

    OnReviewAddListener listener = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_review, container, false);

        titleTv = view.findViewById(R.id.rev_dialog_title_tv);
        reviewTitleEt = view.findViewById(R.id.rev_dialog_title_review_et);
        bodyEt = view.findViewById(R.id.rev_dialog_body_review_et);
        rateEt = view.findViewById(R.id.rev_dialog_rate_review_et);

        addReviewBtn = view.findViewById(R.id.add_review_btn);
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReview();
            }
        });

        return view;
    }

    private void saveReview() {

        String title = reviewTitleEt.getText().toString();
        String body = bodyEt.getText().toString();
        String rate = rateEt.getText().toString();

        if(title.trim().isEmpty() || body.trim().isEmpty() || rate.trim().isEmpty()){

            Snackbar.make(getView(), "All field must be filled", Snackbar.LENGTH_LONG).show();
            return;
        }

        double rateSize = Double.parseDouble(rate);

        if(rateSize > 10 || rateSize < 0){

                Snackbar.make(getView(), "Rate must be between 0 to 10", Snackbar.LENGTH_LONG).show();
            return;
        }

        String username = UserData.getInstance().getUser().getFirstName() + " " + UserData.getInstance().getUser().getLastName();
        MovieReview review = new MovieReview(R.drawable.book_bg, rate+"/10", title, body, username, new Date());
        if(listener != null){
            listener.onAddReview(review);
        }

        dismiss();
    }

    public AddReviewDialogFragment setOnReviewAddListener(OnReviewAddListener listener){
        this.listener = listener;
        return this;
    }


    public interface OnReviewAddListener{
        void onAddReview(MovieReview review);
    }
}

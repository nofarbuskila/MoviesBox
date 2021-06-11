package com.example.nb.androidfinalproject;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;

public class LoginFragment extends Fragment {

    //Firebase
    private FirebaseAuth firebaseAuth;

    private RelativeLayout relativeLayout;

    private EditText emailEt, pswEt;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        initial(rootView);

        return rootView;
    }

    private void initial(View rootView) {

        firebaseAuth = FirebaseAuth.getInstance();

        relativeLayout = rootView.findViewById(R.id.login_relative_layout);

        emailEt = rootView.findViewById(R.id.email_et);
        pswEt = rootView.findViewById(R.id.psw_et);

        progressBar = rootView.findViewById(R.id.progress_bar);

        TextView signUpClick = rootView.findViewById(R.id.signup_tv_clicked);
        signUpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((AuthenticationActivity)getActivity()).goToSignUpFragment();

            }
        });

        Button loginBtn = rootView.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

    }

    private void userLogin(){

        progressBar.setVisibility(View.VISIBLE);

        String email = emailEt.getText().toString().trim();
        String psw = pswEt.getText().toString().trim();

        if(email.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            emailEt.setError("Username is required");
            emailEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            progressBar.setVisibility(View.GONE);
            emailEt.setError("Please enter a valid email");
            emailEt.requestFocus();
            return;
        }

        if(psw.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            pswEt.setError("Password is required");
            pswEt.requestFocus();
            return;
        }

        if(psw.length() < 6){
            progressBar.setVisibility(View.GONE);
            pswEt.setError("Minimum length of password should be 6");
            pswEt.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    ((AuthenticationActivity)getActivity()).goToHome();
                }
                else
                    Snackbar.make(relativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
            }
        });


    }

}

package com.example.nb.androidfinalproject;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nb.androidfinalproject.DataModels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    private EditText firstnameEt, lastnameEt, emailEt, pswEt;
    private ProgressBar progressBar;

    private RadioGroup genderGroup;
    private int radioBtnId;

    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        relativeLayout = rootView.findViewById(R.id.sign_up_relative_layout);

        firstnameEt = rootView.findViewById(R.id.first_name_et);
        lastnameEt = rootView.findViewById(R.id.last_name_et);
        emailEt = rootView.findViewById(R.id.email_et);
        pswEt = rootView.findViewById(R.id.psw_et);

        genderGroup = rootView.findViewById(R.id.gender_group);

        progressBar = rootView.findViewById(R.id.progress_bar);

        initial(rootView);

        return rootView;
    }

    private void initial(View rootView) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Button signupBtn = rootView.findViewById(R.id.sign_up_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        TextView loginClick = rootView.findViewById(R.id.login_tv_clicked);
        loginClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((AuthenticationActivity)getActivity()).goToLoginFragment();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void registerUser(){
        progressBar.setVisibility(View.VISIBLE);

        final String firstName = firstnameEt.getText().toString().trim();
        final String lastName = lastnameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String psw = pswEt.getText().toString().trim();

        String genderSelectedItm = getSelectedGender();

        if(firstName.isEmpty()) {
            firstnameEt.setError("First name is required");
            firstnameEt.requestFocus();
            return;
        }

        if(lastName.isEmpty()) {
            lastnameEt.setError("Last name is required");
            lastnameEt.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            emailEt.setError("Username is required");
            emailEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Please enter a valid email");
            emailEt.requestFocus();
            return;
        }

        if(psw.isEmpty()) {
            pswEt.setError("Password is required");
            pswEt.requestFocus();
            return;
        }

        if(psw.length() < 6){
            pswEt.setError("Minimum length of password should be 6");
            pswEt.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    String userID = currentUser.getUid();
                    String userGender = getSelectedGender();

                    myRef = firebaseDatabase.getReference().child("Users").child(userID);


                    User newUser = new User(firstName, lastName,"default", userGender,null,null);

                    myRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                progressBar.setVisibility(View.GONE);
                                Snackbar.make(relativeLayout, "Sign Up Successfully!", Snackbar.LENGTH_SHORT).show();

                                resetTextView();


                                ((AuthenticationActivity)getActivity()).goToLoginFragment();

                            } else
                                Snackbar.make(relativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);

                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        Snackbar.make(relativeLayout, "Email Already Exist!", Snackbar.LENGTH_SHORT).show();

                    else
                        Snackbar.make(relativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetTextView() {
        firstnameEt.setText("");
        lastnameEt.setText("");
        emailEt.setText("");
        pswEt.setText("");
    }

    private String getSelectedGender(){

        int genderID = genderGroup.getCheckedRadioButtonId();

        switch (genderID){

            case R.id.gender_male:
                return "Male";

            case R.id.gender_female:
                return "Female";

            default:
                return "Other";
        }

    }

}

package com.example.nb.androidfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    //Fragments
    private SignUpFragment signUpFragment;
    private LoginFragment loginFragment;

    private final String SIGN_UP_FRAGMENT_TAG = "SignUpFragment";
    private final String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        firebaseAuth = FirebaseAuth.getInstance();

        signUpFragment = new SignUpFragment();
        loginFragment = new LoginFragment();

        goToLoginFragment();

    }

    public void goToLoginFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_layout, loginFragment, LOGIN_FRAGMENT_TAG)
                .commit();
    }

    public void goToSignUpFragment() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.auth_layout, signUpFragment, SIGN_UP_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void goToHome(){

        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
        }
    }


}

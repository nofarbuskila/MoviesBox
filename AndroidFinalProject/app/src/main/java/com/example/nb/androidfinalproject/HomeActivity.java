package com.example.nb.androidfinalproject;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.nb.androidfinalproject.DataBase.MovieDBRepository;
import com.example.nb.androidfinalproject.DataBase.UserData;
import com.example.nb.androidfinalproject.DataModels.User;
import com.example.nb.androidfinalproject.bottom_navigation_fragments.DiscoverFragment;
import com.example.nb.androidfinalproject.bottom_navigation_fragments.MoviesByGenreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import android.transition.ChangeBounds;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.nb.androidfinalproject.bottom_navigation_fragments.ProfileFragment;
import com.example.nb.androidfinalproject.bottom_navigation_fragments.SearchFragment;
import com.example.nb.androidfinalproject.bottom_navigation_fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FirebaseAuth firebaseAuth;

    private User currentUserData;

    BottomNavigationView bottomNav;

    //Fragments
    private MovieDetailsFragment movieDetailsFragment;
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;
    private SettingsFragment settingsFragment;
    private DiscoverFragment discoverFragment;
    private MoviesByGenreFragment genresFragment;

    private final String DETAILS_FRAGMENT_TAG = "movieDetailsFragment";
    private final String PROFILE_FRAGMENT_TAG = "profileFragment";
    private final String SEARCH_FRAGMENT_TAG = "searchFragment";
    private final String SETTINGS_FRAGMENT_TAG = "settingsFragment";
    private final String HOT_MOVIES_FRAGMENT_TAG = "discoverFragment";
    private final String BY_GENRE_FRAGMENT_TAG = "genresFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        navigationView = findViewById(R.id.slide_navigation);
        navigationView.setNavigationItemSelectedListener(slideNavListener);

        drawerLayout = findViewById(R.id.drawer_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(HomeActivity.this, AuthenticationActivity.class));
        } else {
            setUserData();
        }

        movieDetailsFragment = new MovieDetailsFragment();
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        discoverFragment = new DiscoverFragment();
        genresFragment = new MoviesByGenreFragment();
        settingsFragment = new SettingsFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DiscoverFragment())
                .commit();
    }

    private NavigationView.OnNavigationItemSelectedListener slideNavListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Bundle bundle = new Bundle();

                    switch (menuItem.getItemId()) {
                        case R.id.nav_action:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.ACTION);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_adventure:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.ADVENTURE);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_animation:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.ANIMATION);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_comedy:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.COMEDY);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_drama:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.DRAMA);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_family:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.FAMILY);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_fantasy:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.FANTASY);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_history:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.HISTORY);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_romance:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.ROMANCE);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_war:
                            bundle.putSerializable("genre", MovieDBRepository.eGenres.WAR);
                            goToByGenreFragment(bundle);
                            break;
                        case R.id.nav_setting:
                            settingsFragment.show(getSupportFragmentManager(), SETTINGS_FRAGMENT_TAG);
                            break;
                        case R.id.nav_logout:
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(HomeActivity.this, AuthenticationActivity.class));
                            break;
                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;


                }
            };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_discover:
                            goToHotMoviesFragment();
                            break;
                        case R.id.nav_profile:
                            goToProfileFragment();
                            break;
                        case R.id.nav_search:
                            goToSearchFragment();
                            break;
                        case R.id.nav_more:
                            drawerLayout.openDrawer(GravityCompat.START);
                            break;
                    }
                    return true;
                }
            };

    public void goToMovieDetailsFragment(Bundle bundle, View view) {

        hideKeyboardFrom();
        movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(bundle);

        movieDetailsFragment.setSharedElementEnterTransition(new ChangeBounds());
        movieDetailsFragment.setSharedElementReturnTransition(new ChangeBounds());
        movieDetailsFragment.setEnterTransition(new Fade().setDuration(500));
        movieDetailsFragment.setExitTransition(new Fade().setDuration(500));
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(view,view.getTransitionName())
                .replace(R.id.activity_main_container, movieDetailsFragment, DETAILS_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void goToProfileFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, profileFragment, PROFILE_FRAGMENT_TAG)
                .commit();
    }

    public void goToSearchFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchFragment, SEARCH_FRAGMENT_TAG)
                .commit();
    }

    public void goToHotMoviesFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, discoverFragment, HOT_MOVIES_FRAGMENT_TAG)
                .commit();
    }

    public void goToByGenreFragment(Bundle bundle) {

        genresFragment.setEnterTransition(new Fade());
        genresFragment.setReturnTransition(new Fade());

        genresFragment = new MoviesByGenreFragment();
        genresFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, genresFragment, BY_GENRE_FRAGMENT_TAG)
                .commit();
    }

    private void setUserData(){
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        UserData.getInstance().setUser(user);

                        Log.d("userLogin", "onDataChange");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Error", databaseError.getDetails());
                        Log.d("userLogin", "onCancelled");
                    }
                });
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(bottomNav.getWindowToken(), 0);
    }
}
package com.example.nb.androidfinalproject.bottom_navigation_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nb.androidfinalproject.DataBase.MovieDBRepository;
import com.example.nb.androidfinalproject.DataBase.UserData;
import com.example.nb.androidfinalproject.DataModels.Movie;
import com.example.nb.androidfinalproject.DataModels.User;
import com.example.nb.androidfinalproject.HomeActivity;
import com.example.nb.androidfinalproject.R;
import com.example.nb.androidfinalproject.adapters.HorizontalListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String ERROR_TAG = "ERROR_TAG";

    private static final int SELECT_IMAGE_REQUEST = 1;
    private static final String SELECT_IMAGE = "SELECT_IMAGE";

    private View rootView;

    private RelativeLayout relativeLayout;

    private TextView username_tv, email_tv, gender_tv;
    private ProgressBar progressBar;
    private Button searchBtn;
    private CircularImageView profileImage;
    private ImageButton updateImageProfileBtn;

    private List<Movie> moviesWishList = null;
    private List<Movie> movieWatchList = null;

    private RecyclerView wishRecyclerView;
    private RecyclerView watchRecyclerView;
    private RecyclerView.LayoutManager wishLayoutManager;
    private RecyclerView.LayoutManager watchLayoutManager;
    private HorizontalListAdapter wishListAdapter;
    private HorizontalListAdapter watchListAdapter;

    private Uri uriProfileImage;
    private String profileImageUrl;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private Uri mImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_profile_image");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        User currentUser = UserData.getInstance().getUser();

        progressBar = rootView.findViewById(R.id.profile_fragment_progress_bar);

        username_tv = rootView.findViewById(R.id.username_tv);
        username_tv.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

        email_tv = rootView.findViewById(R.id.email_tv);
        email_tv.setText(firebaseAuth.getCurrentUser().getEmail());

        gender_tv = rootView.findViewById(R.id.gender_tv);
        gender_tv.setText(currentUser.getGender());

        profileImage = rootView.findViewById(R.id.profile_iv);
        setImage(currentUser.getProfileImage());
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        /*updateImageProfileBtn = rootView.findViewById(R.id.update_profile_image_btn);
        updateImageProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });*/

        loadLists();
        return rootView;
    }

    private void loadLists() {
        getlistsFromFirebase();

        buildWishRecycleView();
        buildWatchRecycleView();
    }

    private void openFileChooser() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(getActivity(), this);
    }

    private void setImage(String image) {

        if (image.equals("default")) {

            profileImage.setImageResource(R.drawable.profile_img);
            return;
        }

        Picasso.get()
                .load(image)
                .resize(200, 200)
                .centerCrop()
                .into(profileImage);

    }

    private void uploadFile() {

        if (mImageUri != null) {
            final String uID = firebaseAuth.getCurrentUser().getUid();
            final StorageReference fileRef = mStorageRef.child(uID + ".jpg");

            fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String profileImage = uri.toString();
                                    UserData.getInstance().getUser().setProfileImage(profileImage);

                                    Map updateMap = new HashMap();
                                    updateMap.put("profileImage", profileImage);
                                    mDatabaseRef.child(uID).updateChildren(updateMap);

                                    showSnackbar("Updated Successfully");
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(ERROR_TAG, "ProfileFragment: " + e.getMessage());
                    showSnackbar("Something went wrong... Please try again");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });

        } else {
            Log.d(ERROR_TAG, "ProfileFragment: Image url is null");
            showSnackbar("Something went wrong... Please try again");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                progressBar.setVisibility(View.VISIBLE);
                setImage(mImageUri.toString());

                uploadFile();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Log.d("Error", error.getMessage());

            }
        }



        /*if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData() != null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriProfileImage);
                profileImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

    }

    private void getlistsFromFirebase() {

        List<String> moviesId = UserData.getInstance().getUser().getWishList();
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userData = dataSnapshot.getValue(User.class);
                if (userData != null) {
                    moviesWishList = new ArrayList<>();
                    if (userData.getWishList() != null) {
                        for (String movieId : userData.getWishList()) {
                            parseJson(getContext(), movieId, true);
                        }
                    }

                    movieWatchList = new ArrayList<>();
                    if (userData.getWatchList() != null) {
                        for (String movieId : userData.getWatchList()) {
                            parseJson(getContext(), movieId, false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (moviesId == null || moviesId.size() <= 0) {
            //set text to user

            return;
        }

    }

    private void parseJson(final Context context, String id, final boolean isWishList) {

        MovieDBRepository.getInstance().getMoviesByID(getContext(), id, new MovieDBRepository.ResponseCallBackById() {
            @Override
            public void onSuccess(List<Movie> movie) {

                if (isWishList) {
                    moviesWishList.add(movie.get(0));
                    wishListAdapter.updateData(moviesWishList);
                } else {
                    movieWatchList.add(movie.get(0));
                    watchListAdapter.updateData(movieWatchList);
                }
            }

            @Override
            public void onFail(String Message) {
                Log.d(ERROR_TAG, Message);
                showSnackbar("Something went wrong...");
            }
        });
    }

    private void buildWishRecycleView() {

        if (moviesWishList == null)
            moviesWishList = new ArrayList<>();

        wishRecyclerView = rootView.findViewById(R.id.wishlist_rv);
        wishLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        wishListAdapter = new HorizontalListAdapter(moviesWishList, getContext());

        wishRecyclerView.setLayoutManager(wishLayoutManager);
        wishRecyclerView.setAdapter(wishListAdapter);

        wishListAdapter.setOnItemClickListener(new HorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Log.d("Pos_Tag", "position = " + position + ", size of list is "  + moviesWishList.size());
                Movie currMovie = moviesWishList.get(position);

                Bundle bundle = new Bundle();

                bundle.putString("poster", currMovie.getPosterPath());
                bundle.putString("title", currMovie.getTitle());
                bundle.putString("overview", currMovie.getOverview());
                bundle.putString("release_date", currMovie.getReleaseDate());
                bundle.putString("rating", currMovie.getVote_average());
                bundle.putString("backdrop", currMovie.getBackdropPath());
                bundle.putString("id", currMovie.getId());

                ((HomeActivity) getActivity()).goToMovieDetailsFragment(bundle,v);
            }
        });
    }

    private void buildWatchRecycleView() {

        if (movieWatchList == null)
            movieWatchList = new ArrayList<>();

        watchRecyclerView = rootView.findViewById(R.id.watchlist_rv);
        watchLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        watchListAdapter = new HorizontalListAdapter(movieWatchList, getContext());

        watchRecyclerView.setLayoutManager(watchLayoutManager);
        watchRecyclerView.setAdapter(watchListAdapter);

        watchListAdapter.setOnItemClickListener(new HorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Movie currMovie = movieWatchList.get(position);

                Bundle bundle = new Bundle();

                bundle.putString("poster", currMovie.getPosterPath());
                bundle.putString("title", currMovie.getTitle());
                bundle.putString("overview", currMovie.getOverview());
                bundle.putString("release_date", currMovie.getReleaseDate());
                bundle.putString("rating", currMovie.getVote_average());
                bundle.putString("backdrop", currMovie.getBackdropPath());
                bundle.putString("id", currMovie.getId());

                ((HomeActivity) getActivity()).goToMovieDetailsFragment(bundle, v);
            }
        });
    }

    private void showSnackbar(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

}

package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private static final String TAG = "YourClassName"; // Replace "YourClassName" with the name of your class
    private static final int YOUR_REQUEST_CODE = 1;

    CircleImageView profileImage;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        TextView textView = rootView.findViewById(R.id.edit_profile);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();

        TextView textView1 = rootView.findViewById(R.id.profile_name);
        TextView textView3 = rootView.findViewById(R.id.name_detail);
        TextView textView4 = rootView.findViewById(R.id.dates);
        TextView textView5 = rootView.findViewById(R.id.number);
        TextView textView6 = rootView.findViewById(R.id.country_name);
        TextView textView7 = rootView.findViewById(R.id.city_name);

        DocumentReference docRef = db.collection("details").document("OTrKZXR6ROzlNnMxyIdG");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String first_name = documentSnapshot.getString("first_name");
                String middle_name = documentSnapshot.getString("middle_name");
                String surname = documentSnapshot.getString("surname");
                String phone_number = documentSnapshot.getString("phone_number");
                String date_of_birth = documentSnapshot.getString("date_of_birth");
                String country = documentSnapshot.getString("country");
                String city = documentSnapshot.getString("city");

                String fullName = first_name + " " +surname;
                textView1.setText(fullName);
                textView3.setText(middle_name);
                textView4.setText(date_of_birth);
                textView5.setText(phone_number);
                textView6.setText(country);
                textView7.setText(city);
            } else {
                Log.d(TAG, "No such document");
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Error fetching document: " + e);
            Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
        });

        profileImage = rootView.findViewById(R.id.profile_image);
        FloatingActionButton fab = rootView.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(v -> ImagePicker.with(AccountFragment.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        );

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                uploadImageToFirebaseStorage(imageUri);
            }
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("profile_images/" + UUID.randomUUID().toString());

        UploadTask uploadTask = imagesRef.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return imagesRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                if (downloadUri != null) {
                    String imageUrl = downloadUri.toString();
                    saveImageUrlToFirebaseDatabase(imageUrl);
                }
            } else {
                // Handle unsuccessful upload
                Log.e(TAG, "Failed to upload image");
            }
        });
    }


    private void saveImageUrlToFirebaseDatabase(String imageUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("details")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("profileImageUrl");

        databaseRef.setValue(imageUrl)
                .addOnSuccessListener(aVoid -> retrieveImageUrlFromFirebaseDatabase())
                .addOnFailureListener(e -> Log.e(TAG, "Failed to save image URL to database", e));
    }

    private void retrieveImageUrlFromFirebaseDatabase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("details")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("profileImageUrl");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.getValue(String.class);

                    Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.placeholder_image)
                            .circleCrop()
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error retrieving image URL from database", error.toException());
            }
        });
    }
}
//Connecting Activity for waiting the User until finding the match
package com.example.visitorapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityConnectingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ConnectingActivity extends AppCompatActivity {

    //    Enabling View binding here
    ActivityConnectingBinding binding;

    //    Initiating Firebase Auth
    FirebaseAuth firebaseAuth;

    //    initiating firebase database
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityConnectingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });









        //        Getting instance of the Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

//        getting instance of the Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

//        Getting current user from the firebase
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

//        Getting profile Url from the MainActivity class
        String profileUrl = getIntent().getStringExtra("profile");

        // Glide Library for Image loading form Firebase Database
        Glide.with(ConnectingActivity.this)
//               loading profile image form UserModel.class
                .load(profileUrl)
//                into the profileImage
                .into(binding.profileImage);

//        Getting unique id of the User form Firebase Database
        String userName = firebaseAuth.getUid();

//        creating room in firebase database
        firebaseDatabase.getReference()
//                searching from on the basis of child id
                .orderByChild("users")
//                if the child id or room id is 0 then its available and limit it to first
                .equalTo(0).limitToFirst(1)
//                adding addListenerForSingleValueEvent for the event result
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                        if the getChildrenCount is less than 1 then room id is available
                        if (snapshot.getChildrenCount() > 0){
//                            Room is available
                            Log.e("Error", "Available");
                        }else {
//                            Room is not available

//                          instead of creating another class for storing data on firebase, We can used HashMap
//                            HashMap<String,Object> map = new HashMap<>();

                            HashMap<String,Object> hashMap = new HashMap<>();
//                         id of incoming root in firebase database
                            hashMap.put("incoming",userName);
//                           who is createdBy root in firebase database
                            hashMap.put("createdBy",userName);
//                         is user  isAvailable root in firebase database
                            hashMap.put("isAvailable",true);
//                            status of the room root in firebase database
                            hashMap.put("status",0);

//                            storing data inside the Firebase database
                            firebaseDatabase.getReference()
                                    .child("users")
                                    .child(userName)
//                                    listening and checking this room
                                    .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

//                                            storing data inside the Firebase database
                                            firebaseDatabase.getReference()
                                                    .child("users")
                                                    .child(userName)
//                                                 listening and checking to
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                                            checking is the users exist or not via snapshot
                                                            if (snapshot.child("status").exists()){

//                                                                if the child status is 1, then starting intent form ConnectingActivity To CallActivity
                                                                if (snapshot.child("status").getValue(Integer.class) == 1){

//                                                                    if status of the child is 1, and passing some data
                                                                    Intent intent = new Intent(ConnectingActivity.this,CallActivity.class);

//                                                                    passing userName to CallActivity
                                                                    intent.putExtra("userName",userName);
//                                                                    passing incoming id to CallActivity
                                                                    intent.putExtra("incoming",snapshot.child("incoming").getValue(String.class));
//                                                                    passing createdBy to CallActivity
                                                                    intent.putExtra("createdBy",snapshot.child("createdBy").getValue(String.class));
//                                                                    passing isAvailable 0 or 1 to CallActivity
                                                                    intent.putExtra("isAvailable",snapshot.child("isAvailable").getValue(boolean.class));
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }






}
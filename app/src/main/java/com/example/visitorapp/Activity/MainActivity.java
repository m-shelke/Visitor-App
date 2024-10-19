//MainActivity is the Dashboard of the App User
package com.example.visitorapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.visitorapp.Model.UserModel;
import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity {

//    Enabling ViewBinding here
    ActivityMainBinding binding;

//    initiating firebase Authentication here
    FirebaseAuth firebaseAuth;

//    initiating Firebase Database
    FirebaseDatabase firebaseDatabase;

//    coins count variable
    long coins = 0;

//    request Code for permission
    private int requestCode = 1;

//    Get permission for the User and permission are always in String
    String[] permissionArr = new String[] {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        inflating .xml file with java code
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        binding root of the .xml
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


//        Getting instance of the FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

//        Getting instance of the Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

//        Getting current User form firebase database
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

//        getting the reference of the firebase database
        firebaseDatabase.getReference()
                .child("profiles")
//                getting User ID from the Firebase
                        .child(currentUser.getUid())
//                ValueEventListener for data change in the Firebase database
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                        converting firebase database obj to UserModel.class
                                        UserModel userModel = snapshot.getValue(UserModel.class);

//                                        getting coins form UserModel.class
                                        coins = userModel.getCoins();

//                                        setting this coins to coinscountTxv
                                        binding.coinscountTxv.setText("You Have : " + coins);

                                        // Glide Library for Image loading form Firebase Database
                                        Glide.with(MainActivity.this)
//                                              loading profile image form UserModel.class
                                                .load(userModel.getProfile())
//                                               into the profileImage
                                                .into(binding.profileImage);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


//        find id of getStartedBtn from .xml and setOnClickListener to handle finding match partner
        binding.letsFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            //Overriding onClick abstract method
            public void onClick(View v) {

//                if the permission is granted, then .......
                if (isPermissionGranted()) {

//                if the coins is greater than 5, then show following Toast message
                    if (coins > 5) {
//                    if the coins is more than 5, directly jumped to the ConnectingActivity
                        startActivity(new Intent(MainActivity.this, ConnectingActivity.class));

//                    Toast.makeText(MainActivity.this, " Finding Your Match ", Toast.LENGTH_SHORT).show();
//                    if the coins is less than 5, suggest to Earn coins by watching Ads
                    } else {
                        Toast.makeText(MainActivity.this, "Coins Are Not Enough For Match, Earn Coins Now", Toast.LENGTH_SHORT).show();
                    }
//                    or if permission is not granted then ask for permission
                } else {
//                    calling askPermission() method here
                    askPermission();
                }
            }
        });


    }

//    fun for requesting the permission form Users
    public void askPermission(){
        //request for permission accessed
        ActivityCompat.requestPermissions(this,permissionArr,requestCode);
    }

//  fun for  checking is permission granted or not form the device
    private boolean isPermissionGranted(){
//       performing to check the permission for each loop
        for (String permissionArr : permissionArr){
//            if the permission is not granted then return false
            if (ActivityCompat.checkSelfPermission(this,permissionArr) !=PackageManager.PERMISSION_GRANTED){
                return false;
            }
            return true;
        }
//        by default permission is true
        return true;
    }
}
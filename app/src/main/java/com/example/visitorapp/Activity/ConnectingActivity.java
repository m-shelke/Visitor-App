//Connecting Activity for waiting the User until finding the match
package com.example.visitorapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityConnectingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class ConnectingActivity extends AppCompatActivity {

    //    Enabling View binding here
    ActivityConnectingBinding binding;

    //    Initiating Firebase Auth
    FirebaseAuth firebaseAuth;

    //    initiating firebase database
    FirebaseDatabase firebaseDatabase;

    boolean isOkay = false;


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

     //   Intent mainIntent = getIntent();

//      String profileUrl1 =  mainIntent.getStringExtra("profile");

//        String profileUrl = getIntent().getStringExtra("profile");

//        Glide.with(this)
//                .load(profileUrl1)
//                .into(binding.profileImage);

        //        Getting unique id of the User form Firebase Database
        String userName = firebaseAuth.getUid();

        firebaseDatabase.getReference().child("users")
//                searching from on the basis of child id
                .orderByChild("status")
//                if the child id or room id is 0 then its available and limit it to first
                .equalTo(0).limitToFirst(1)
//                adding addListenerForSingleValueEvent for the event result
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


//                        if the getChildrenCount is less than 1 then room id is available
                        if (snapshot.getChildrenCount() > 0) {

//                            Room is available
                            isOkay = true;

//                            getting the children of Firebase Database
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){

//                                getting reference of the Firebase Database
                                firebaseDatabase.getReference()
//                                      getting  path string of the "users"
                                        .child("users")
//                                        fetching it's value from key "users" and value "incoming"
                                        .child(Objects.requireNonNull(dataSnapshot.getKey()))
                                        .child("incoming")
//                                        set value as "userName"
                                        .setValue(userName);

//                                getting reference of the Firebase Database
                                firebaseDatabase.getReference()
//                                      getting  path string of the "users"
                                        .child("users")
//                                        fetching it's value from key "users" and value "status"
                                        .child(dataSnapshot.getKey())
                                        .child("status")
//                                        set value 1 for intent CallActivity
                                        .setValue(1);


//                                getting the value of "incoming" from firebase database
                                String incoming = dataSnapshot.child("incoming").getValue(String.class);
//                                getting the value of "createdBy" from firebase database
                                String createdBy = dataSnapshot.child("createdBy").getValue(String.class);
//                                Getting isAvailable in boolean true or false value from the Firebase Database
                                boolean isAvailable = Boolean.TRUE.equals(dataSnapshot.child("isAvailable").getValue(Boolean.class));


//                                if status of the child is 1, and passing some data
                                Intent intent = new Intent(ConnectingActivity.this,CallActivity.class);

//                                passing key and value data from ConnectingActivity to CallActivity
                                intent.putExtra("userName",userName);
                                intent.putExtra("incoming",incoming);
                                intent.putExtra("createdBy",createdBy);
                                intent.putExtra("isAvailable",isAvailable);

//                                starting activity here
                                startActivity(intent);
//                                this activity gonna finished here
                             //   finish();

                            }

                            Log.e("Error", "Available");

                        }else {
//                            Room is not available

//                            instead of creating another class for storing data on firebase, We can used HashMap
//                            HashMap<String,Object> map = new HashMap<>();

//                            creating room id in firebase database
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



                                                                    if (isOkay)
                                                                        return;

                                                                    isOkay = true;

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
//                                                                    this activity will finished here
                                                                  //  finish();
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

//    if the users Back Pressed, then Room Id generated in the Firebase Database, gonna deleted


    @Override
    protected void onDestroy() {
        delete();
        super.onDestroy();
    }

    //    creating a  delete(); for, if the user Back Pressed on ConnectingActivity, then the Room ID gonna Deleted
    public void delete(){
        firebaseDatabase.getReference().child("users")
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ConnectingActivity.this, "Room ID Is Deleted..!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //    After deleting User Account data, we launch LoginActivity activity
    private void startLoginActivity(){
        startActivity(new Intent(ConnectingActivity.this,MainActivity.class));
        finish();
    }
}

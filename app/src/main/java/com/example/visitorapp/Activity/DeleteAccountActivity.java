package com.example.visitorapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityDeleteAccountBinding;
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

import java.util.Objects;

public class DeleteAccountActivity extends AppCompatActivity {

//    Enabling ActivityBinding here
    ActivityDeleteAccountBinding binding;

    //ProgressDialog to show while deleting user details, app data and User info
     ProgressDialog progressDialog;

    //TAG for logs in logcat, showing error via this TAG
    private static final String TAG="DELETE_ACCOUNT_TAG";

    //FirebaseAuth instance for Auth related task
     FirebaseAuth firebaseAuth;

    //    FirebaseUser instance for getting CurrentUser ID and it's related task
     FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        inflating .xml file with this Java Code
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
//        Binding root of the .xml file
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //init/setup progressDialog to while deleting account
        progressDialog = new ProgressDialog(this);
//        main title of the progressDialog
        progressDialog.setTitle("Please Wait..");
//        side clickable of the Activity UI, make it's false
        progressDialog.setCanceledOnTouchOutside(false);

        //Initiating  FirebaseAuth here
        firebaseAuth = FirebaseAuth.getInstance();

        //Initiating FirebaseUser here
        currentUser = firebaseAuth.getCurrentUser();

        //Handle toolbarBackBtn back click, to go back
        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Handle confirmDeleteBtn click, to Delete User Account and App data as well on User Click
        binding.confirmDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });

//        binding.verifyAccountBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                verifyAccount();
//            }
//        });

    }

//    creating function, for deleting the user details and Email Id login
    private void deleteAccount() {

//        log to check the functionality
        Log.d(TAG, "deleteAccount: ");

//        getting current user id of the user, in currentUser variable
        currentUser = firebaseAuth.getCurrentUser();

//        this is another way of, getting currentUser Login ID
      ///  String myUid = firebaseUser.getUid();

        //set progressDialog message, this....
        progressDialog.setMessage("Deleting User Account..");
//        and here, show the progressDialog
        progressDialog.show();

//        calling delete(); method, to delete the User account and details
        currentUser.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //        log to check the functionality
                        Log.d(TAG, "onSuccess: Account Deleted Successfully ");

                        //set progressDialog message, this....
                        progressDialog.setMessage("Deleting User App Data ");

//                        DatabaseReference object for, fetching and and remove or deleting user data, that is inside the Firebase. Root is "profiles"
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles");
//                        child of the Root is "userId" of the User id (Uid)
                        reference.orderByChild("userId").equalTo(currentUser.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                        for each loop, for removing the children value as well
                                        for (DataSnapshot ds : snapshot.getChildren()) {
//                                            as reference got, deleting value of it
                                            ds.getRef().removeValue();
                                        }

                                        //set progressDialog message, this....
                                        progressDialog.setMessage("Deleting User Data..");

//                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles");
//                                        databaseReference.child(currentUser.getUid())
//                                                .removeValue()
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//
//                                                        //Account data deleted
//                                                        Log.d(TAG, "onSuccess: User data deleted successfully..");
//                                                        startMainActivity();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//
//                                                        Log.e(TAG, "onFailure: ", e);
//                                                        progressDialog.dismiss();
//                                                        Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                        startMainActivity();
//                                                    }
//                                                });

//                                        calling startLoginActivity(); method here
                                        startLoginActivity();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        progressDialog.dismiss();
                        Toast.makeText(DeleteAccountActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }



    //    After deleting User Account data, we launch LoginActivity activity
    private void startLoginActivity(){
        Log.d(TAG, "startMainActivity: ");
        startActivity(new Intent(DeleteAccountActivity.this,LoginActivity.class));
        finish();
    }

    //    If the user press back button, then we launch MainActivity
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this, MainActivity.class));
//        finishAffinity();
//    }







//
//    private void verifyAccount() {
//        Log.d(TAG, "verifyAccount: ");
//        //show progressDialog
//        progressDialog.setMessage("Sending Account Verification Instruction To Your Email ");
//        progressDialog.show();
//        //send account/email verification instruction to the registered email
//        firebaseAuth.getCurrentUser().sendEmailVerification()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    //instruction send, check email, sometimes it goes to the spam folder so if not into inbox please check the spam folder
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "onSuccess: ");
//                        progressDialog.dismiss();
//
//                        Toast.makeText(DeleteAccountActivity.this, "Account Verification Instruction Send To Your Email ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //failed to send instruction
//                        Log.e(TAG, "onFailure: ",e);
//                        progressDialog.dismiss();
//                        Toast.makeText(DeleteAccountActivity.this, "Failed Due To "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
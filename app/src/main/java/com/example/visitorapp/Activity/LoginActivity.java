//Login Activity for Google Sign
package com.example.visitorapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.visitorapp.Model.UserModel;
import com.example.visitorapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

//    Enabling GoogleSignInClient for Google ID sign-in
    GoogleSignInClient googleSignInClient;

//    store requestCode_SignIn as a 11 in Integer
    int requestCode_SignIn = 11;

//    initiating Firebase Authentication
    FirebaseAuth firebaseAuth;

//    initiating Realtime Firebase database
    FirebaseDatabase firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        getting instance of the Firebase here
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
//            calling goToNextActivity method
            goToNextActivity();
        }

//       getting instance of the RealTime Firebase Database here
        firebaseDatabase = FirebaseDatabase.getInstance();

        //For accessed of the Google Account, we have provide some Data and Tokens to the Google. It can done vie GoogleSignInOption
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //automatically fetch it form Google_services.json file
                .requestIdToken(getString(R.string.default_web_client_id))
                //request Email account to Google
                        .requestEmail()
                                .build();

        //GoogleSignInClient act as client between application and Google server and it get ID of the User form google server
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        //find id of logingBtn from .xml and setOnClickListener to handle Google Sign Account
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            //Overriding onClick abstract method
            @Override
            public void onClick(View v) {

                //getting Google SignIn Intent for this activity
                Intent intent = googleSignInClient.getSignInIntent();
                //and starting activity here
                startActivityForResult(intent,requestCode_SignIn);


//                For now, we just go LoginActivity to MainActivity via Intent class
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));       //commenting starting code, that was for testing purpose
//                finish the stack of activity here
//                finish();

            }
        });
    }

//     (when user chose one of Google Account form the list of Google Account list) From onActivityResult, we get Data of User which he/she clicked on Google Account
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if the requestCode from onActivityResult match with startActivityForResult
        if (requestCode == requestCode_SignIn){
//            then run the following task and getting Google SignedIn Account data From getSignInIntent with onActivityResult
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

//            getting result of the GoogleSignInAccount and store it to "account" variable
            GoogleSignInAccount account = task.getResult();

//            calling authWithGoogle method here and passing idToken via account.getIdToken
            authWithGoogle(account.getIdToken());

        }
    }

    public void authWithGoogle(String idToken){

//        GoogleAuthProvider will gives Authentication Credential and store inside credential and passed it to method parameter called "idToken"
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);

        //By passing credential to Firebase and signIn With Credential
        firebaseAuth.signInWithCredential(credential)
//                addOnCompleteListener for if signIn With Credential completed
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

//                        if the task is completed, then it Successful otherwise it's failed
                        if (task.isSuccessful()){
                            //and here, we get the actual User.   This
                          //  FirebaseUser user = task.getResult().getUser();

//                            or and getting current user form firebase
                            FirebaseUser user1 = firebaseAuth.getCurrentUser();

                            //Getting user data form firebase and sent it to the UserModel Java class
                            UserModel firebaseUser = new UserModel(user1.getUid(),user1.getDisplayName(),user1.getPhotoUrl().toString(),"Unkown",500);

                            firebaseDatabase.getReference()
                                    .child("profiles")
//                                    Specific and Unique id of User
                                    .child(user1.getUid())
                                    .setValue(firebaseUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if the task of uploading data to Firebase Realtime database is successful then.....
                                            if (task.isSuccessful()){
                                               startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                               and finished the all back end stack activity
                                                finishAffinity();
                                            }else {
                                                //In case problem occurred in the Firebase Realtime Database for uploading the data for that Toast message
                                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

//                            Getting Google Account Profile Photo Url form the Firebase and Google Server
                            assert user1 != null;
                            Log.e("Profile", Objects.requireNonNull(user1.getPhotoUrl()).toString());
                        }else {
//                            In case, error occurred then display in logcat
                            Log.e("Error",task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    //     method for Intent of LoginActivity to MainActivity
    public void goToNextActivity(){
//        Go LoginActivity to MainActivity via Intent class
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//        finish stack of the Activity here
        finish();
    }
}
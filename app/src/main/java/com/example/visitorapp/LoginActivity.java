//Login Activity for Google Sign
package com.example.visitorapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginActivity extends AppCompatActivity {

    //Enabling GoogleSignInClient for Google ID sign-in
    GoogleSignInClient googleSignInClient;

    //store requestCode_SignIn as a 11 in Integer
    int requestCode_SignIn = 11;

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

        //For accessed of the Google Account, we have provide some Data and Tokens to the Google. It can done vie GoogleSignInOption
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //automatically fetch it form Google_services.json file
                .requestIdToken(getString(R.string.default_web_client_id))
                //request Email account to Google
                        .requestEmail()
                                .build();

        //GoogleSignInClient act as client between application and Google server and it get ID of the User form google server
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        //find id of logingBtn from .xml and setOnClickListener to handle Google Sign Account
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            //Overriding onClick abstract method
            @Override
            public void onClick(View v) {

                //getting Google SignIn Intent for this activity
                Intent intent = googleSignInClient.getSignInIntent();
                //and starting activity here
                startActivityForResult(intent,requestCode_SignIn);


                //For now, we just go LoginActivity to MainActivity via Intent class
               // startActivity(new Intent(LoginActivity.this,MainActivity.class));       //commenting starting code, that was for testing purpose
                //finish the stack of activity here
               // finish();
            }
        });
    }
}
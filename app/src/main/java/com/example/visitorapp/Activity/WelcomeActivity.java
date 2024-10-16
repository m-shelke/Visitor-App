package com.example.visitorapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.visitorapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

//    initiating Firebase Authentication here
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//              getting instance of the FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

//        if the firebaseAuth is not null, then go to next activity that is MainActivity
        if (firebaseAuth.getCurrentUser() != null){
//            calling goToNextActivity method
            goToNextActivity();
        }

//        find id of getStartedBtn from .xml and setOnClickListener to jump to LoginActivity
        findViewById(R.id.getStartedBtn).setOnClickListener(new View.OnClickListener() {
            //Overriding onClick abstract method
            @Override
            public void onClick(View v) {
//        calling goToNextActivity method
                goToNextActivity();
            }
        });


    }

//     method for Intent of WelcomeActivity to LoginActivity
    public void goToNextActivity(){
//        Go WelcomeActivity to LoginActivity via Intent class
        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
//        finish stack of the Activity here
                finish();
    }
}
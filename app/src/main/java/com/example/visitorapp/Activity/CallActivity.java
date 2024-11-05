//Call Activity for Video and Audio Conversation
package com.example.visitorapp.Activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.visitorapp.Model.InterfaceJava;
import com.example.visitorapp.Model.UserModel;
import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityCallBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class CallActivity extends AppCompatActivity {

//    Enabling viewbinding here
    ActivityCallBinding binding;

//    setting unique id of the user from firebase
    String uniqueId = "";

//    getting unique id of the user form the firebase
    FirebaseAuth firebaseAuth;

//    Getting userName form the "Connecting Activity"
    String userName = "";

//    Getting other stranger User Name from the Firebase database
    String strangerUserName = "";

//    checking is peer is connected or not and by default is false
    boolean isPeerConnected = false;

//    Getting database reference
    DatabaseReference databaseReference;

//    checking isAudio is audible or not and by default it's true
    boolean isAudio = true;

//    checking isVideo is visible or not and by default it's also true
    boolean isVideo = true;

//    Getting the User id or User Name that who is created this stream call
    String createdBy;

//    checking is page exist or not
    boolean pageExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        inflating .xml
        binding = ActivityCallBinding.inflate(getLayoutInflater());
//        attaching root with java code
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callJavaScriptFunction1("javascript:getAudioOutput()");
            }
        });

//        getting instance of the firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

//        storing unique id of the user
//        uniqueId = firebaseAuth.getUid();

//        getting instance of the FirebaseDatabaseReference of the firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

//        getting userName from "Connecting Activity"
        userName = getIntent().getStringExtra("userName");

//        getting incoming from "Connecting Activity"
        String incoming = getIntent().getStringExtra("incoming");

//        getting createdBy from "Connecting Activity"
        createdBy = getIntent().getStringExtra("createdBy");

//        By default strange user name is empty
//        strangerUserName = incoming;
//
////        if incoming is equals to "strangerUserName" then set incoming to strangerUserName
//        assert incoming != null;
//        if (incoming.equalsIgnoreCase(strangerUserName)){
            strangerUserName = incoming;

//            calling "setUpWebView" method here
            setUpWebView();

//            handling onClicked event on mic button
            binding.micBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    setting isAudio to is not Audio
                    isAudio = !isAudio;
//                    calling javascript function here to run the javascript function okay
                    callJavaScriptFunction("javascript:toggleAudio(\""+isAudio+"\")");

//                    if Audio is not play, then show Unmute Audio image
                    if (isAudio){
//                        setting un-mute audio image to micBtn
                        binding.micBtn.setImageResource(R.drawable.microphone_active);
                    }else {
//                        setting mute image resource to the micBtn
                        binding.micBtn.setImageResource(R.drawable.not_active_microphone);
                    }

                }
            });

//             handling onClicked event on videoBtn button
            binding.videoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    setting isVideo to is not Video
                    isVideo = !isVideo;
//                    calling javascript function here to run the javascript function okay
                    callJavaScriptFunction("javascript:toggleVideo(\""+isVideo+"\")");

//                    if Video is not play, then show Un-mute Video image
                    if (isVideo){
//                        setting video image to micBtn
                        binding.videoBtn.setImageResource(R.drawable.active_video);
                    }else {
//                        setting mute video image resource to the videoBtn
                        binding.videoBtn.setImageResource(R.drawable.not_actvie_video);
                    }
                }
            });

//            setOnClickListener for listening the event of callendBtn
            binding.callendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if the callendBtn is clicked then, we finish this activity
                    finish();
                }
            });



//        Orr
//       uniqueId = getUniqueId();
    }

//    setup of WebView Client
    @SuppressLint("SetJavaScriptEnabled")
    public void setUpWebView(){

//        setting web view client here
        binding.webView.setWebChromeClient(new WebChromeClient(){

//            sending Audio and Video permission form WebView client to the device
            @Override
            public void onPermissionRequest(PermissionRequest request) {
//               super.onPermissionRequest(request);
               
//               if the android version is .........
//                grating all permission, that was demanding form wetWebclient
                request.grant(request.getResources());
            }
        });

//        enabling JavaScriptEnabled
        binding.webView.getSettings().setJavaScriptEnabled(true);
//        setMediaPlaybackRequiresUserGesture it's false
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        implementing java interface
        binding.webView.addJavascriptInterface(new InterfaceJava(this),"Android");

//        calling loadVideoCall(); method
        loadVideoCall();
    }

    public void loadVideoCall(){
//        loading "call.html" file from "assets" folder
        String filepath = "file:android_asset/calll.html";  // Or file explorer address of file   D:\Android Studio Program\Projects\Random\app\src\main\assets

//        file:android_asset/call.html

//        and load it to the webview
        binding.webView.loadUrl(filepath);

//       webview client for  initialising peers
        binding.webView.setWebViewClient(new WebViewClient(){

//            onPageFinished for Notify the host application that a page has finished loading. This method is call only for main frame
//            connecting peers after loading the "call.html"
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                initialing peers here
                initializePeer();
            }
        });
    }

    private void initializePeer() {
//        getting unique id for every user form method called "getUniqueId()"
        uniqueId = getUniqueId();

//        calling "callJavaScriptFunction" function here for running the javaScript function inside the  Android Studio
        callJavaScriptFunction("javascript:init(\"" + uniqueId + "\")");

//        if the roomId is created by the current user, then get another person user id
        if (createdBy.equalsIgnoreCase(userName)){

//            if the page is exit then return here. And go back from here
            if (pageExit)
                return;

//            setting another person unique id to connId
            databaseReference.child(userName).child("connId").setValue(uniqueId);


            Log.e("Error",uniqueId );

//            if it's available then, set it to true
            databaseReference.child(userName).child("isAvailable").setValue(true);

//            setting visibility of the Group of loadingGroup as a GONE
            binding.loading.setVisibility(View.GONE);

//            Visible controls here and by default it's was Invisible to the User
            binding.group.setVisibility(View.VISIBLE);

//            setting Profile,User name and City of the Stranger
            FirebaseDatabase.getInstance().getReference()
//                    inside the profiles
                    .child("profiles")
//                    strangerUserName
                    .child(strangerUserName)
//                    addListenerForSingleValueEvent to listen the value that inside in Firebase Database
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

//                            getting name,profile and city name form UserModel. And UserModel is obj of Firebase Database
                            UserModel userModel = snapshot.getValue(UserModel.class);

//                            setting profile image to ImageView via Glide Library
                            Glide.with(CallActivity.this)
                                    .load(userModel.getProfile())
                                    .into(binding.profileImg);

//                            setting profile name to name textview
                            binding.profileName.setText(userModel.getName());
//                            setting city name to city textview
                            binding.cityName.setText(userModel.getCity());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }else {
//            otherwise if the room id is not created by the current user then
//            Handler for 5 second delay, to check the who is crated this Room Id
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    setting createdBy id to strangerUserName
                    strangerUserName = createdBy;

//            setting Profile,User name and City of the Stranger
                    FirebaseDatabase.getInstance().getReference()
//                    inside the profiles
                            .child("profiles")
//                    strangerUserName
                            .child(strangerUserName)
//                    addListenerForSingleValueEvent to listen the value that inside in Firebase Database
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

//                            getting name,profile and city name form UserModel. And UserModel is obj of Firebase Database
                                    UserModel userModel = snapshot.getValue(UserModel.class);

//                            setting profile image to ImageView via Glide Library
                                    Glide.with(CallActivity.this)
                                            .load(userModel.getProfile())
                                            .into(binding.profileImg);

//                            setting profile name to name textview
                                    binding.profileName.setText(userModel.getName());
//                            setting city name to city textview
                                    binding.cityName.setText(userModel.getCity());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(strangerUserName)
                            .child("connId")
//                            single Value listener for one time, to check
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                    if the "connId" is not null
                                    if (snapshot.getValue() != null){
//                                        then sending call request to another connId
//                                        calling this sendCallRequest(); method
                                        sendCallRequest();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            },3000);
        }
    }

    public void onPeerConnected(){
//        updating to isPeerConnected = true;
        isPeerConnected = true;
    }

//    creating a sendCallRequest(); method for sending the call request to ConnId
    void sendCallRequest(){
//        if the Peer is not Connected, then show this Toast message
        if (!isPeerConnected) {
            Toast.makeText(this, "You'r Not Connected. Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            return;
        }

//            calling listenConnId(); method here
            listenConnId();

    }

//    creating listenConnId(); function for listening the connId
    void listenConnId(){

//        fetching "connId" from the firebase database
        databaseReference.child(strangerUserName).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                if the value of snapshot is null then return it
                if (snapshot.getValue() == null) {
                    return;
                }

//                setting visibility of the Group of loadingGroup as a GONE
                binding.loading.setVisibility(View.GONE);

//                    setting visibility of the Group of controls as a Visible
                    binding.group.setVisibility(View.VISIBLE);
//                getting connId from the firebase database
                String connId = snapshot.getValue(String.class);
//                calling javascript function to start the video call
                callJavaScriptFunction("javascript:startCall(\""+connId+"\")");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    creating this function for calling and for the purpose of JavaScript function
    void callJavaScriptFunction(String function){

//        for the ui clickable event handle
        binding.webView.post(new Runnable() {
            @Override
            public void run() {
//                Enabling JavaScript Function in the Android Studio
                binding.webView.evaluateJavascript(function,null);     // resultCallback of the function set as null. we won't it for now
            }
        });
    }

    void callJavaScriptFunction1(final String function) {
        // Ensure the WebView is initialized properly
        if (binding.webView != null && binding.webView.getUrl() != null) {
            binding.webView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Log the JavaScript function for debugging purposes
                        Log.d("JS Function", "Evaluating: " + function);

                        // Execute JavaScript and get the result via callback
                        binding.webView.evaluateJavascript(function, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // The value is the result returned from the JavaScript function
                                Log.d("JS Result", value);

                                // Now, you can process the result data here
                                display(value);
                            }
                        });
                    } catch (Exception e) {
                        // Handle any errors that might occur during JavaScript execution
                        Log.e("WebViewError", "Error evaluating JavaScript: " + e.getMessage());
                    }
                }
            });
        } else {
            // Handle case where WebView is not initialized or URL is null
            Log.e("WebViewError", "WebView is not initialized or URL is null.");
        }
    }

    void display(String result) {
        try {
            // If the result is JSON data, parse it using JSONObject
            JSONObject jsonResult = new JSONObject(result);

            JSONArray name = jsonResult.getJSONArray("name");
            for (int i=0;i<name.length();i++){
                Log.i("AudioOutPutDevices",name.getString(i));
            }

            // Do something with the data
//            Log.d("JavaScript Data", "Name: " + name + ", Age: " + age);
        } catch (Exception e) {
            Log.e("JavaScript Data Error", "Error parsing JSON: " + e.getMessage());
        }
    }




    //    an "UUID" is a 'Universally Unique Identifier' standardized 128-bit format for a String ID used to Uniquely Identify Information. It's used to uniquely identify the Your Application Bluetooth service
    String getUniqueId(){
        return UUID.randomUUID().toString();
    }

//    calling onDestroy method here


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        pageExit is now true
        pageExit = true;
//        setting the value of "createdBy" to "null"
        databaseReference.child(createdBy).setValue(null);
//        and finish this activity here
        finish();
    }
}
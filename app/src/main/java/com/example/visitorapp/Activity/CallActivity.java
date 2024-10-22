//Call Activity for Video and Audio Conversation
package com.example.visitorapp.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.visitorapp.Model.InterfaceJava;
import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityCallBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class CallActivity extends AppCompatActivity {

    //    Enabling viewbinding here
    ActivityCallBinding binding;

    //    setting unique id of the user from firebase
    String uniqueId = "";

    //    getting unique id of the user form the firebase
    FirebaseAuth firebaseAuth;


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

//        getting instance of the firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

//        storing unique id of the user
//        uniqueId = firebaseAuth.getUid();

//        Orr
//        uniqueId = getUniqueId();         completely depends on you
    }

    //    setup of WebView Client
    @SuppressLint("SetJavaScriptEnabled")
    public void setUpWebView() {

//        setting web view client here
        binding.webView.setWebChromeClient(new WebChromeClient() {

            //            sending Audio and Video permission form WebView client to the device
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
//                grating all permission, that was demanding form wetWebclient
                request.grant(request.getResources());
            }
        });

//        enabling JavaScriptEnabled
        binding.webView.getSettings().setJavaScriptEnabled(true);
//        setMediaPlaybackRequiresUserGesture it's false
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        implementing java interface
        binding.webView.addJavascriptInterface(new InterfaceJava(this), "Android");

//        calling loadVideoCall(); method
        //loadVideoCall();
    }


//    public void loadVideoCall(){
////        loading "call.html" file from "assets" folder
//        String filepath = "file:android_assets/call.html";   // Or file explorer address of file   D:\Android Studio Program\Projects\Random\app\src\main\assets
//
////        and load it to the webview
//        binding.webView.loadUrl(filepath);
//
////       webview client for  initialising peers
//        binding.webView.setWebViewClient(new WebViewClient(){
//
////            onPageFinished for Notify the host application that a page has finished loading. This method is call only for main frame
////            connecting peers after loading the "call.html"
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
////                initialing peers here
//                initializePeer();
//            }
//        });
//    }

//    private void initializePeer() {
////        getting unique id for every user form method called "getUniqueId()"
//        uniqueId = getUniqueId();
//    }


//    an "UUID" is a 'Universally Unique Identifier' standardized 128-bit format for a String ID used to Uniquely Identify Information. It's used to uniquely identify the Your Application Bluetooth service
//    String getUniqueId(){
//        return UUID.randomUUID().toString();
//    }
//
}
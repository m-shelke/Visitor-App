package com.example.visitorapp.Model;

import android.webkit.JavascriptInterface;

import com.example.visitorapp.Activity.CallActivity;

public class InterfaceJava {

//    Notify when two peers get connected
    CallActivity callActivity;

//    Constructor with CallActivity parameter
    public InterfaceJava(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

//    onPeerConnected method
    @JavascriptInterface
    public void onPeerConnected(){
//        calling onPeerConnected() from callActivity
        callActivity.onPeerConnected();
    }


}

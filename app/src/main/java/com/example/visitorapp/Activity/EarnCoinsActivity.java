package com.example.visitorapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.visitorapp.R;
import com.example.visitorapp.databinding.ActivityEarnCoinsBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class EarnCoinsActivity extends AppCompatActivity {

//    Enabling View-binding here
    ActivityEarnCoinsBinding binding;

//    initiating RewardedAd here
    private RewardedAd rewardedAd;

//    initiating Firebase Database
    FirebaseDatabase firebaseDatabase;

//    getting current User ID in this variable
    String currentUid;

//    coins variable for increasing the coins after watching Advertisement
    int coins = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        inflating .xml via View-Binding
        binding = ActivityEarnCoinsBinding.inflate(getLayoutInflater());
//        attaching root via View-binding
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Getting instance of the FirebaseDatabase here
        firebaseDatabase = FirebaseDatabase.getInstance();
//        getting currentUid form the FirebaseDatabase inside currentUid
        currentUid = FirebaseAuth.getInstance().getUid();

//        calling loadAds(); method here
        loadAds();

//        Getting current user coins from the Firebase Database instead of passing data form MainActivity because of do not pirating and hacking by third party application
        firebaseDatabase.getReference().child("profiles")
                .child(currentUid)
                        .child("coins")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

//                                        getting value of the coins from the Firebase Database
                                        coins = snapshot.getValue(Integer.class);
//                                        setting that current user coins value to coinscountTxv (.xml)
                                        binding.coinscountTxv.setText(String.valueOf(" Currently You Owned : " +coins));

                                    }

                                    @Override
                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                    }
                                });

//        loading ads on Ads1 clicked
        binding.Ads1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if rewardedAd is not null
                if (rewardedAd != null) {
                    Activity activityContext = EarnCoinsActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.

//                            calling loadAds(); for even after watching one Ad, then it will be ready for next clicked as well
                            loadAds();
//                            granted reward as a +20 coins to Firebase Database
                            coins = coins + 20;
//                          after watching Ad, we increase the coins value to Firebase Database
                            firebaseDatabase.getReference().child("profiles")
                                            .child(currentUid)
                                                    .child("coins")
//                            setting that current user coins value to coinscountTxv (.xml)
                                    .setValue(coins);

//                            changing icon of adsVideo1 imageview after completion of Watching Ads
                            binding.adsVideo1.setImageResource(R.drawable.double_check);
//                              log for message in LogCat
                            Log.d("TAG", "The user earned the reward.");

                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    //                              log for message in LogCat
                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                }
            }
        });

        //        loading ads on Ads2 clicked
        binding.Ads2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if rewardedAd is not null
                if (rewardedAd != null) {
                    Activity activityContext = EarnCoinsActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.

//                            calling loadAds(); for even after watching one Ad, then it will be ready for next clicked as well
                            loadAds();
//                            granted reward as a +20 coins to Firebase Database
                            coins = coins + 50;
//                          after watching Ad, we increase the coins value to Firebase Database
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentUid)
                                    .child("coins")
//                            setting that current user coins value to coinscountTxv (.xml)
                                    .setValue(coins);

//                            changing icon of adsVideo1 imageview after completion of Watching Ads
                            binding.adsVideo2.setImageResource(R.drawable.double_check);
//                              log for message in LogCat
                            Log.d("TAG", "The user earned the reward.");

                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    //                              log for message in LogCat
                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                }
            }
        });

        //        loading ads on Ads1 clicked
        binding.Ads3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if rewardedAd is not null
                if (rewardedAd != null) {
                    Activity activityContext = EarnCoinsActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.

//                            calling loadAds(); for even after watching one Ad, then it will be ready for next clicked as well
                            loadAds();
//                            granted reward as a +20 coins to Firebase Database
                            coins = coins + 120;
//                          after watching Ad, we increase the coins value to Firebase Database
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentUid)
                                    .child("coins")
//                            setting that current user coins value to coinscountTxv (.xml)
                                    .setValue(coins);

//                            changing icon of adsVideo1 imageview after completion of Watching Ads
                            binding.adsVideo3.setImageResource(R.drawable.double_check);
//                              log for message in LogCat
                            Log.d("TAG", "The user earned the reward.");

                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    //                              log for message in LogCat
                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                }
            }
        });

        //        loading ads on Ads1 clicked
        binding.Ads4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if rewardedAd is not null
                if (rewardedAd != null) {
                    Activity activityContext = EarnCoinsActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.

//                            calling loadAds(); for even after watching one Ad, then it will be ready for next clicked as well
                            loadAds();
//                            granted reward as a +20 coins to Firebase Database
                            coins = coins + 150;
//                          after watching Ad, we increase the coins value to Firebase Database
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentUid)
                                    .child("coins")
//                            setting that current user coins value to coinscountTxv (.xml)
                                    .setValue(coins);

//                            changing icon of adsVideo1 imageview after completion of Watching Ads
                            binding.adsVideo4.setImageResource(R.drawable.double_check);
//                              log for message in LogCat
                            Log.d("TAG", "The user earned the reward.");

                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    //                              log for message in LogCat
                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                }
            }
        });

        //        loading ads on Ads1 clicked
        binding.Ads5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if rewardedAd is not null
                if (rewardedAd != null) {
                    Activity activityContext = EarnCoinsActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.

//                            calling loadAds(); for even after watching one Ad, then it will be ready for next clicked as well
                            loadAds();
//                            granted reward as a +20 coins to Firebase Database
                            coins = coins + 200;
//                          after watching Ad, we increase the coins value to Firebase Database
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentUid)
                                    .child("coins")
//                            setting that current user coins value to coinscountTxv (.xml)
                                    .setValue(coins);

//                            changing icon of adsVideo1 imageview after completion of Watching Ads
                            binding.adsVideo5.setImageResource(R.drawable.double_check);
//                              log for message in LogCat
                            Log.d("TAG", "The user earned the reward.");

                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    //                              log for message in LogCat
                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                }
            }
        });

    }

//    creating a loadAds() method for loading the Ads
    void loadAds(){

//        Requesting AdRequest
        AdRequest adRequest = new AdRequest.Builder().build();
//        loading rewarded Ad via Google AdMob Account Id or Application ID
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
//                RewardedAdLoadCallback method
                adRequest, new RewardedAdLoadCallback() {

//            if Ad get Failed to load
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
//                        then rewarded Ad is null
                        rewardedAd = null;
                    }
//                        onAdLoaded(); this method is loading actual Ad
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
//                    loading rewardedAd here
                        rewardedAd = ad;
//                              log for message in LogCat
                        Log.d("TAG", "Ad was loaded.");
                    }
                });
    }
}
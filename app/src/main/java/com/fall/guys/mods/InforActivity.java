package com.fall.guys.mods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.scary.teacher.guide.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class InforActivity extends AppCompatActivity {

    private ImageView imgInformation, btnDownload;
    private TextView txtInformationTitle, txtInformationSubTitle;
    private ProgressBar bnp;

    private String img, download;

    private String introduceTitle1 = "Fall Guys: Ultimate Knockout is a 2020 platformer battle royale game developed by Mediatonic and published by Devolver Digital.";
    private String introduceSubTitle1 = "Fall Guys Map base on Fall Guys game.";

    private String introduceTitle2 = "if you have skills at map-making then make a part of minecraft the same of that game “Fall Guys”";
    private String introduceSubTitle2 = "Features\n" +
            "You can ride them (You must tame them by giving a crown)\n" +
            "They have sounds link\n" +
            "Coming in 15 colours\n" +
            "For now they don’t have specific spawn rules, you must get them by creative or commands\n" +
            "It’s a obj model but I was able to add animations at walking\n" +
            "If you ride one you won’t appear above your scale goes to 0.0 (To simule that you are one)";

    private String introduceTitle3 = "Fall Guys: Ultimate Knockout is a 2020 platformer battle royale game developed by Mediatonic and published by Devolver Digital.";
    private String introduceSubTitle3 = "Fall Guys Skin base on Fall Guys game.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));

        imgInformation = findViewById(R.id.imgDetailInformation);
        btnDownload = findViewById(R.id.btnDownload);
        txtInformationTitle = findViewById(R.id.txtInformationTitle);
        txtInformationSubTitle = findViewById(R.id.txtInformationSubTitle);
        bnp = findViewById(R.id.progressBar);
        btnDownload = findViewById(R.id.btnDownload);

        img = getIntent().getStringExtra("img");
        download = getIntent().getStringExtra("download");


        switch (img){
            case "1.png":
            case "2.png":
                txtInformationTitle.setText(introduceTitle1);
                txtInformationSubTitle.setText(introduceSubTitle1);
                break;
            case "mod.png":
                txtInformationTitle.setText(introduceTitle2);
                txtInformationSubTitle.setText(introduceSubTitle2);
                txtInformationSubTitle.setTextSize(12);
                break;
            case "4.png":
                txtInformationTitle.setText(introduceTitle3);
                txtInformationSubTitle.setText(introduceSubTitle3);
                break;
        }

        View adContainer = findViewById(R.id.adMobView);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(StartActivity.BANNER_ID);
        ((RelativeLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                btnDownload.setVisibility(View.VISIBLE);
                bnp.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                btnDownload.setVisibility(View.VISIBLE);
                bnp.setVisibility(View.GONE);
            }

        });

        Random r = new Random();
        int ads = r.nextInt(100);

        if (ads >= StartActivity.PERCENT_SHOW_BANNER_AD){
            mAdView.destroy();
            mAdView.setVisibility(View.GONE);
        }

        AssetManager am = getAssets();
        try {
            InputStream is = am.open(img);
            Drawable d = Drawable.createFromStream(is, null);

            Glide
                    .with(this)
                    .load(d)
                    .centerCrop()
                    .into(imgInformation);
        } catch (IOException e) {

        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(InforActivity.this, DownloadActivity.class);
                videoIntent.putExtra("img", img);
                videoIntent.putExtra("download", download);
                videoIntent.putExtra("name", getIntent().getStringExtra("name"));
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(videoIntent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
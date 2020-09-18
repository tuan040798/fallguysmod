package com.fall.guys.mods;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.util.IOUtils;
import com.scary.teacher.guide.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DownloadActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    private String downloadguide1 = "1. Accept the application external storage access request.\n" +
            "2. Install Maps then you can see path.\n" +
            "3. Extract file and then copy the world folder to the following path: /storage/emulated/0/games/com.mojang/minecraftWorlds/. It might take a few seconds for it to process.\n" +
            "4. Launch MCPE and select the new world to play.";

    private String downloadguide2 = "1. Accept the application external storage access request.\n" +
            "2. Install Mod then you can see path.\n" +
            "3. Launch Minecraft Pocket Edition and open up the BlockLauncher menu and select “Manage ModPE Scripts”.\n" +
            "4. Find .mcpack or .mcaddon file then click import. The screen might go black for a while but just wait for it to load and it’s installed \n" +
            "5. Apply the resource pack when creating new world or editing existing world";

    private String downloadguide3 = "1. Accept the application external storage access request.\n" +
            "2. Install Skin then you can see path.\n" +
            "3. Open up Minecraft Pocket Edition and tap on the clothing hanger icon below the player character.\n" +
            "4. Tap on the empty space next to the Alex and Steve skins. \n" +
            "5. Next a new button should appear above the player model called Choose New Skin. Tap on it once to open a file manager and select the downloaded skin. It’s most probably in your downloads folder.\n" +
            "6. Choose skin and Confirm Skin.";

    private TextView txtTitle, txtDownloadPath;
    private String img, download;
    private ImageView btnInstall;
    private String download_url;

    private ProgressDialog pDialog;
    private ProgressBar bnp;
    public static final int progress_bar_type = 0;

    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));

        txtTitle = findViewById(R.id.txtDownloadTitle);
        btnInstall = findViewById(R.id.btnInstall);
        txtDownloadPath = findViewById(R.id.txtDownloadPath);
        bnp = findViewById(R.id.progressBar);

        txtTitle.setText(downloadguide1);

        img = getIntent().getStringExtra("img");
        download = getIntent().getStringExtra("download");

        switch (img){
            case "1.png":
            case "2.png":
                txtTitle.setText(downloadguide1);
                break;
            case "mod.png":
                txtTitle.setText(downloadguide2);
                break;
            case "4.png":
                txtTitle.setText(downloadguide3);
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
                btnInstall.setVisibility(View.VISIBLE);
                bnp.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                btnInstall.setVisibility(View.VISIBLE);
                bnp.setVisibility(View.GONE);
            }

        });

        Random r = new Random();
        int ads = r.nextInt(100);

        if (ads >= StartActivity.PERCENT_SHOW_BANNER_AD){
            mAdView.destroy();
            mAdView.setVisibility(View.GONE);
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(StartActivity.INTER_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int i) {

            }

        });

        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DownloadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ_ACCESS_PERMISSION);
                } else {
                    installFile();
                }


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installFile();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void installFile(){

        new DownloadFileFromURL().execute(download);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                String [] downloadname = download.split("/");
                String download_storage_name = downloadname[downloadname.length - 1];

                download_url = Environment
                        .getExternalStorageDirectory().toString()
                        + "/"+ getString(R.string.app_name) + "/" +download_storage_name;

                // Output stream
                OutputStream output = new FileOutputStream(download_url);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            txtDownloadPath.setText("Download Success: " + download_url);
            dismissDialog(progress_bar_type);

        }

    }


    private void loadAds() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            if(isOnline()){
                loadAds();
            } else {
            }
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


    @Override
    public boolean onSupportNavigateUp() {
        showInterstitial();
        onBackPressed();
        return true;
    }
}
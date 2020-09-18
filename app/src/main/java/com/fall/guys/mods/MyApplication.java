package com.fall.guys.mods;

import android.app.Application;

import com.csmltowsin.adx.service.AdsExchange;
import com.facebook.ads.AudienceNetworkAds;
import com.flurry.android.FlurryAgent;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "FFCKXT6S5GGM89DZWHCZ");
        AudienceNetworkAds.initialize(this);
        AdsExchange.init(this, "5f64842149ca095ee7ce7dd3");
    }
}

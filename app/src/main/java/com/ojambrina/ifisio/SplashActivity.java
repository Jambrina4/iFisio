package com.ojambrina.ifisio;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ojambrina.ifisio.UI.login.LoginActivity;

import static com.ojambrina.ifisio.utils.Constants.SPLASH_DISPLAY_LENGTH;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    Context context;
    //AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //if (appPreferences.getLogin != null) {
                    intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                //} else {
                //    intent = new Intent(context, HomeActivity.class);
                //    startActivity(intent);
                //    finish();
                //}
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}

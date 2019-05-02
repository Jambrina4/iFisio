package com.ojambrina.ifisio;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ojambrina.ifisio.UI.HomeActivity;
import com.ojambrina.ifisio.UI.login.LoginActivity;

import static com.ojambrina.ifisio.utils.Constants.SPLASH_DISPLAY_LENGTH;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    Context context;
    //AppPreferences appPreferences;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    //TODO implementar metodo comprobación usuario en firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();

                //authStateListener = new FirebaseAuth.AuthStateListener() {
                //    @Override
                //    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //        FirebaseUser user = firebaseAuth.getCurrentUser();
                //        if (user != null) {
                //            Intent intent = new Intent(context, HomeActivity.class);
                //            startActivity(intent);
                //            finish();
                //        } else {
                //            Intent intent = new Intent(context, LoginActivity.class);
                //            startActivity(intent);
                //            finish();
                //        }
                //    }
                //};
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}

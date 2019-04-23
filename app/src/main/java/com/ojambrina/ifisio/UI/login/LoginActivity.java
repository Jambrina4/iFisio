package com.ojambrina.ifisio.UI.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ojambrina.ifisio.R;

public class LoginActivity extends AppCompatActivity {

    //TODO Añadir Butterknife

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        listeners();
    }

    private void listeners() {

    }
}

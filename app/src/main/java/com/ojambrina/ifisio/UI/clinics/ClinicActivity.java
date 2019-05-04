package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context context;
    AppCompatActivity contextForToolbar;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;
        utils = new Utils();


        setToolbar();
        listeners();
    }

    private void setToolbar() {
        utils.configToolbar(contextForToolbar, toolbar);
    }

    private void listeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

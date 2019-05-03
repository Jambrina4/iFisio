package com.ojambrina.ifisio.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.ConnectClinicActivity;
import com.ojambrina.ifisio.UI.clinics.CreateClinicActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_add_clinic)
    LinearLayout layoutAddClinic;
    @BindView(R.id.layout_connect_clinic)
    LinearLayout layoutConnectClinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        context = this;

        listeners();
    }

    private void listeners() {
        layoutAddClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateClinicActivity.class);
                startActivity(intent);
            }
        });

        layoutConnectClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConnectClinicActivity.class);
                startActivity(intent);
            }
        });
    }


}

package com.ojambrina.ifisio.UI.clinics.patients;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Context context;
    private AppCompatActivity contextForToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        ButterKnife.bind(this);

        contextForToolbar = this;
        context = this;

        setToolbar();
    }

    private void setToolbar() {
        Utils.configToolbar(contextForToolbar, toolbar);
        toolbar.setTitle("Rehabilitación");
    }
}

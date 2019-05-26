package com.ojambrina.ifisio.UI.clinics.patients.patientDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.ViewPagerAdapter;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;

public class PatientDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;

    private Context context;
    private AppCompatActivity contextForToolbar;
    private Intent intent;
    private String patientName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    private String clinic_name;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        ButterKnife.bind(this);

        intent = getIntent();
        patientName = intent.getStringExtra(PATIENT_NAME);
        clinic_name = intent.getStringExtra(CLINIC_NAME);

        contextForToolbar = this;
        context = this;

        setToolbar();
        setFirebase();
        getPatient();
        setAdapter();
        listeners();
    }

    private void setToolbar() {
        Utils.configToolbar(contextForToolbar, toolbar);
        toolbar.setTitle("Rehabilitación");
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    private void setAdapter() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), context, patient);
        tabLayout.setupWithViewPager(pager);
        pager.setAdapter(viewPagerAdapter);
    }

    private void getPatient() {
        firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //TODO pasar la información a los fragments correspondientes y adaptar los pager

                patient = documentSnapshot.toObject(Patient.class);
                if (patient != null) {
                    String test = patient.getName();
                    Log.d("TEST", test);
                }
            }
        });
    }

    private void listeners() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}

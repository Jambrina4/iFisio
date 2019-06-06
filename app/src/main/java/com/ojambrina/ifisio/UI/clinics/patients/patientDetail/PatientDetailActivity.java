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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.ViewPagerAdapter;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.utils.Utils;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;
import static com.ojambrina.ifisio.utils.Constants.SPLASH_DISPLAY_LENGTH;

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
    private ViewPagerAdapter viewPagerAdapter;

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

        setFirebase();
        setAdapter();
        getPatientData();
        setToolbar();
        listeners();
    }

    private void getPatientData() {
        firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen failed.", e);
                    return;
                }

                patient = documentSnapshot.toObject(Patient.class);
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), context, clinic_name, patientName, firebaseFirestore);
        tabLayout.setupWithViewPager(pager);
        pager.setAdapter(viewPagerAdapter);
    }

    private void listeners() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}

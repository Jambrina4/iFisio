package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.PatientAdapter;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINIC;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;

public class ClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Context context;
    AppCompatActivity contextForToolbar;
    Utils utils;
    Patient patient;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    Intent intent;
    String clinic_name, name;
    PatientAdapter patientAdapter;
    Clinic clinic;
    List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;
        utils = new Utils();
        intent = getIntent();
        clinic_name = intent.getStringExtra(CLINIC_NAME);
        clinic = (Clinic) intent.getSerializableExtra(CLINIC);
        patientList = new ArrayList<>();

        //recibir clinica entera

        setToolbar();
        setFirebase();
        setAdapter();
        listeners();
    }

    //DiffUtils librería
    private void setAdapter() {

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatient();
                firebaseFirestore.collection("clinicas").document(clinic_name).set(clinic);
            }
        });

        firebaseFirestore.collection("clinicas").document("Clinica Test 3").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Clinic clinic = documentSnapshot.toObject(Clinic.class);

                    if (clinic != null && clinic.getPatientList() != null) {
                        patientAdapter.setData(clinic.getPatientList());
                    }
                }
            }
        });
    }

    private void addPatient() {
        patient = new Patient();

        name = "Nombre Test";

        patient.setName(name);
        patient.setSurname("Apellido 1 Apellido2");
        patient.setAge("22");
        patient.setIdentityNumber("12345678H");
        patient.setInjury("Tendinitis");
        patient.setTreatment("Descargas");
        patient.setVisit("15-05-2019");

        patientList.add(patient);

        clinic.setPatientList(patientList);
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

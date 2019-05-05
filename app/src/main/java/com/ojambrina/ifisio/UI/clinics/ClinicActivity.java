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
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Context context;
    AppCompatActivity contextForToolbar;
    Utils utils;
    Patient patient;
    Map<String, Patient> patients;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    Intent intent;
    String clinic_name, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;
        utils = new Utils();
        intent = getIntent();
        clinic_name = intent.getStringExtra("clinic_name");

        setToolbar();
        setFirebase();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatient();

                firebaseFirestore.collection("clinicas").document(clinic_name).collection("Lista de pacientes").document(name).set(patients);
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

        patients = new HashMap<>();
        patients.put(name, patient);
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

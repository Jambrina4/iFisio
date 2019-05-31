package com.ojambrina.ifisio.UI.clinics.patients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;

public class AddPatient extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_patient_name)
    EditText editPatientName;
    @BindView(R.id.edit_patient_surname)
    EditText editPatientSurname;
    @BindView(R.id.edit_patient_identity_number)
    EditText editPatientIdentityNumber;
    @BindView(R.id.edit_patient_age)
    EditText editPatientAge;
    @BindView(R.id.button_add_patient)
    Button buttonAddPatient;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    private Intent intent;
    private String clinic_name;
    private Patient patient;
    private Context context;
    private AppCompatActivity contextForToolbar;
    private HashMap<String, Patient> patientHashMap;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        ButterKnife.bind(this);

        contextForToolbar = this;
        context = this;

        setToolbar();
        setFirebase();
        getData();
        listeners();
    }

    private void listeners() {
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatient();
                firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(name).set(patient);
                //firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(name).set(patientHashMap);
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getData() {
        intent = getIntent();
        clinic_name = intent.getStringExtra(CLINIC_NAME);
    }

    public void addPatient() {
        patient = new Patient();

        name = editPatientName.getText().toString().trim();

        patient.setName(name);
        patient.setSurname(editPatientSurname.getText().toString().trim());
        patient.setAge(editPatientAge.getText().toString().trim());
        patient.setIdentityNumber(editPatientIdentityNumber.getText().toString().trim());

        //patientHashMap = new HashMap<>();
        //patientHashMap.put(name, patient);
    }

    private void setToolbar() {
        Utils.configToolbar(contextForToolbar, toolbar);
    }
}

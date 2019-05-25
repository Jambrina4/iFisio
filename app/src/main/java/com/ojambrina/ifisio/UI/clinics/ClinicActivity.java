package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.patients.AddPatient;
import com.ojambrina.ifisio.adapters.PatientAdapter;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.ADD_PATIENT_REQUEST_CODE;
import static com.ojambrina.ifisio.utils.Constants.CLINIC;
import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;

public class ClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_clinic)
    RecyclerView recyclerClinic;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Context context;
    AppCompatActivity contextForToolbar;
    Patient patient;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    Intent intent;
    String clinic_name, name;
    PatientAdapter patientAdapter;
    Clinic clinic;
    List<String> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;
        intent = getIntent();
        clinic_name = intent.getStringExtra(CLINIC_NAME);
        clinic = (Clinic) intent.getSerializableExtra(CLINIC);
        patientList = new ArrayList<String>();
        //recibir clinica entera

        setToolbar();
        setFirebase();
        setAdapter();
        setPatientList();
        listeners();
    }

    //DiffUtils librería
    private void setAdapter() {
        patientAdapter = new PatientAdapter(context, patientList);
        GridLayoutManager layout = new GridLayoutManager(context,2 );
        recyclerClinic.setLayoutManager(layout);
        recyclerClinic.setAdapter(patientAdapter);
    }

    private void setToolbar() {
        Utils.configToolbar(contextForToolbar, toolbar);
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
            }
        });
    }

    private void setPatientList() {
        firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    patientList.addAll(list);
                    progressBar.setVisibility(View.GONE);
                    recyclerClinic.setVisibility(View.VISIBLE);
                    patientAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void addPatient() {
        Intent intent = new Intent(context, AddPatient.class);
        intent.putExtra(CLINIC_NAME, clinic_name);
        startActivityForResult(intent, ADD_PATIENT_REQUEST_CODE);
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

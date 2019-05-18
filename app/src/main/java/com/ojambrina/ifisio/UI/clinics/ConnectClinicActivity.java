package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.ClinicAdapter;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;

public class ConnectClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_clinic)
    RecyclerView recyclerClinic;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.text_no_clinic)
    TextView textNoClinic;

    List<String> clinicList;
    ClinicAdapter clinicAdapter;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    Context context;
    AppCompatActivity contextForToolbar;
    Utils utils;
    Clinic clinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_clinic);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;
        utils = new Utils();

        clinicList = new ArrayList<>();

        setToolbar();
        listeners();
        setFirebase();
        setClinicList();
        setAdapter();
    }

    private void listeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textNoClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateClinicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setToolbar() {
        utils.configToolbar(contextForToolbar, toolbar);
    }

    private void setClinicList() {

        //TODO: guardar en shared preferences la lista de clinicas que he añadido para filtrar y que solo salgan las mías

        firebaseFirestore.collection(CLINICS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    clinicList.addAll(list);
                    progressBar.setVisibility(View.GONE);
                    recyclerClinic.setVisibility(View.VISIBLE);
                    clinicAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(CLINICS);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setAdapter() {
        clinicAdapter = new ClinicAdapter(context, clinicList);
        recyclerClinic.setAdapter(clinicAdapter);
    }
}

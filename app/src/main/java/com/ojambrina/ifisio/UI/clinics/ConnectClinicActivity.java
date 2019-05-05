package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.ClinicAdapter;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_clinic)
    RecyclerView recyclerClinic;

    Clinic clinic;
    List<Clinic> clinicList;
    ClinicAdapter clinicAdapter;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    Context context;
    AppCompatActivity contextForToolbar;
    Utils utils;

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
    }

    private void setToolbar() {
        utils.configToolbar(contextForToolbar, toolbar);
    }

    private void setClinicList() {
        firebaseFirestore.collection("clinicas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //TODO Convertir el resultado en el objeto necesario para imprimirlo en pantalla
                    firebaseFirestore.collection("clinicas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("GET CLINICS", document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d("GET CLINICS", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setAdapter() {
        clinicAdapter = new ClinicAdapter(context, clinicList);
        recyclerClinic.setAdapter(clinicAdapter);
    }
}

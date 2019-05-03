package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.ClinicAdapter;
import com.ojambrina.ifisio.entities.Clinic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_clinic)
    RecyclerView recyclerClinic;

    List<Clinic> clinicList;
    ClinicAdapter clinicAdapter;
    DatabaseReference databaseReference;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_clinic);
        ButterKnife.bind(this);

        context = this;

        clinicList = new ArrayList<>();

        setClinicList();
        setAdapter();
    }

    private void setClinicList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("clinicas");

        databaseReference.child("listado_clinicas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clinicList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    clinicList.add((Clinic) data.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("WARNING", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void setAdapter() {
        clinicAdapter = new ClinicAdapter(context, clinicList);
        recyclerClinic.setAdapter(clinicAdapter);
    }
}

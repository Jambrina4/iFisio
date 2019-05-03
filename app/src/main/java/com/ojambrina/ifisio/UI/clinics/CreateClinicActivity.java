package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Clinic;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateClinicActivity extends AppCompatActivity {

    Context context;
    @BindView(R.id.edit_clinic_name)
    EditText editClinicName;
    @BindView(R.id.edit_clinic_direction)
    EditText editClinicDirection;
    @BindView(R.id.button_clinic_register)
    Button buttonClinicRegister;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clinic);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        context = this;

        listeners();
    }

    private void listeners() {
        buttonClinicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editClinicName.getText().toString().trim();
                String direction = editClinicDirection.getText().toString().trim();
                addClinic(name, direction);

                //Intent intent = new Intent(context, ClinicActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void addClinic(String name, String direction) {
        Clinic clinic = new Clinic();

        clinic.setName(name);
        clinic.setDirection(direction);

        databaseReference.child("clinicas").child("listado_clinicas").setValue(clinic);
    }
}

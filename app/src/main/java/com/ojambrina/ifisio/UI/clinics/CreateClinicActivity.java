package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Clinic;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINIC;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;

public class CreateClinicActivity extends AppCompatActivity {

    //Butterknife
    @BindView(R.id.edit_clinic_name)
    EditText editClinicName;
    @BindView(R.id.edit_clinic_password)
    EditText editClinicPassword;
    @BindView(R.id.edit_clinic_direction)
    EditText editClinicDirection;
    @BindView(R.id.edit_clinic_identity_number)
    EditText editClinicIdentityNumber;
    @BindView(R.id.edit_clinic_description)
    EditText editClinicDescription;
    @BindView(R.id.button_clinic_register)
    Button buttonClinicRegister;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Declarations
    Context context;
    Clinic clinic;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    String name, password, direction, clinicIdentityNumber, description;
    HashMap<String, Clinic> clinicHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clinic);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        context = this;

        setToolbar();
        setFirebase();
        listeners();
    }

    private void listeners() {
        buttonClinicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClinic();

                //TODO Revisar la forma de almacenar la información para poder recuperarla correctamente

                firebaseFirestore.collection("clinicas").document(name).set(clinicHashMap);
                Toast.makeText(context, "Clínica agregada correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ClinicActivity.class);
                intent.putExtra(CLINIC_NAME, clinic.getName());
                intent.putExtra(CLINIC, clinic);
                startActivity(intent);
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addClinic() {
        clinic = new Clinic();

        name = editClinicName.getText().toString().trim();
        password = editClinicPassword.getText().toString().trim();
        direction = editClinicDescription.getText().toString().trim();
        clinicIdentityNumber = editClinicIdentityNumber.getText().toString().trim();
        description = editClinicDescription.getText().toString().trim();

        clinic.setName(name);
        clinic.setPassword(password);
        clinic.setDirection(direction);
        clinic.setIdentityNumber(clinicIdentityNumber);
        clinic.setDescription(description);

        clinicHashMap = new HashMap<>();
        clinicHashMap.put(name, clinic);
    }

    private void setFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

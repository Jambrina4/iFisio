package com.ojambrina.ifisio.UI.clinics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.HomeActivity;
import com.ojambrina.ifisio.entities.Clinic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINIC;
import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_LIST;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.LATEST_CLINIC;
import static com.ojambrina.ifisio.utils.Constants.SHARED_PREFERENCES;

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
    boolean isValidClinicName, isValidClinicPassword;
    SharedPreferences sharedPreferences;
    List<Clinic> clinicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clinic);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        context = this;
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        setToolbar();
        setFirebase();
        listeners();
    }

    private void listeners() {
        buttonClinicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrings();

                validateClinicPassword(editClinicPassword);
                validateClinicName(editClinicName);

                if (isValidClinicName && isValidClinicPassword) {
                    firebaseFirestore.collection(CLINICS).document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                Toast.makeText(context, "Ya existe una clínica con ese nombre", Toast.LENGTH_SHORT).show();
                            } else {
                                addClinic();
                                if (sharedPreferences.getString(CLINIC_LIST, "").isEmpty()) {
                                    clinicList.clear();
                                    Gson gson = new Gson();
                                    clinicList.add(clinic);
                                    String listOfClinics = gson.toJson(clinicList);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(CLINIC_LIST, listOfClinics);
                                    editor.putString(LATEST_CLINIC, clinic.getName());
                                    editor.apply();
                                } else {
                                    clinicList.clear();
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString(CLINIC_LIST, "");

                                    Type type = new TypeToken<List<Clinic>>(){}.getType();
                                    List<Clinic> clinicList = gson.fromJson(json, type);

                                    clinicList.add(clinic);
                                    String listOfClinics = gson.toJson(clinicList);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(CLINIC_LIST, listOfClinics);
                                    editor.putString(LATEST_CLINIC, clinic.getName());
                                    editor.apply();
                                }

                                firebaseFirestore.collection(CLINICS).document(name).set(clinic);
                                Toast.makeText(context, "Clínica agregada correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.putExtra(CLINIC_NAME, clinic.getName());
                                intent.putExtra(CLINIC, clinic);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getStrings() {
        name = editClinicName.getText().toString().trim();
        password = editClinicPassword.getText().toString().trim();
        direction = editClinicDescription.getText().toString().trim();
        clinicIdentityNumber = editClinicIdentityNumber.getText().toString().trim();
        description = editClinicDescription.getText().toString().trim();
    }

    private void addClinic() {
        clinic = new Clinic();

        clinic.setName(name);
        clinic.setPassword(password);
        clinic.setDirection(direction);
        clinic.setIdentityNumber(clinicIdentityNumber);
        clinic.setDescription(description);
    }

    private void setFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    //VALIDATIONS
    private void validateClinicName(EditText editClinicName) {
        name = editClinicName.getText().toString().trim();
        if (name.length() > 0) {
            isValidClinicName = true;
        } else {
            editClinicName.requestFocus();
            editClinicName.setError("El campo nombre no puede estar vacío");
            isValidClinicName = false;
        }
    }

    private void validateClinicPassword(EditText editClinicPassword) {
        password = editClinicPassword.getText().toString().trim();
        if (password.length() >= 8) {
            isValidClinicPassword = true;
        } else {
            editClinicPassword.requestFocus();
            editClinicPassword.setError("Al menos 8 caracteres");
            isValidClinicPassword = false;
        }
    }
}

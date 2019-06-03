package com.ojambrina.ifisio.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.CreateClinicActivity;
import com.ojambrina.ifisio.UI.clinics.patients.AddPatient;
import com.ojambrina.ifisio.UI.login.LoginActivity;
import com.ojambrina.ifisio.adapters.ClinicAdapter;
import com.ojambrina.ifisio.adapters.PatientAdapter;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.utils.AppPreferences;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;

public class HomeActivity extends AppCompatActivity {

    //Butterknife
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar_patients)
    ProgressBar progressBarPatients;
    @BindView(R.id.recycler_patients)
    RecyclerView recyclerPatients;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.text_email)
    TextView textEmail;
    @BindView(R.id.layout_user)
    LinearLayout layoutUser;
    @BindView(R.id.separator1)
    View separator1;
    @BindView(R.id.progress_bar_drawer)
    ProgressBar progressBarDrawer;
    @BindView(R.id.recycler_clinic)
    RecyclerView recyclerClinic;
    @BindView(R.id.separator2)
    View separator2;
    @BindView(R.id.text_add_clinic)
    TextView textAddClinic;
    @BindView(R.id.separator3)
    View separator3;
    @BindView(R.id.text_connect_clinic)
    TextView textConnectClinic;
    @BindView(R.id.separator4)
    View separator4;
    @BindView(R.id.text_logout)
    TextView textLogout;
    @BindView(R.id.separator5)
    View separator5;
    @BindView(R.id.text_settings)
    TextView textSettings;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    //Declarations
    List<Clinic> clinicList = new ArrayList<>();
    List<String> patientList = new ArrayList<>();
    ClinicAdapter clinicAdapter;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    Context context;
    AppCompatActivity contextForToolbar;
    PatientAdapter patientAdapter;
    Clinic clinic;
    String clinicName;
    String clinicPassword;
    AppPreferences appPreferences;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;
        appPreferences = new AppPreferences();

        clinicName = "Clinica Alfa";

        setFirebase();
        setToolbar();

        setDrawerAdapter();
        setPatientAdapter();

        setClinicList();
        setPatientList();

        drawerListeners();
        patientListeners();
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(CLINICS);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setToolbar() {
        Utils.configToolbar(contextForToolbar, toolbar);
        textEmail.setText(firebaseUser.getEmail());
    }

    private void setDrawerAdapter() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));

        clinicAdapter = new ClinicAdapter(context, clinicList, new ClinicAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Clinic clinic) {
                clinicName = clinicList.get(position).getName();

            }
        });
        recyclerClinic.setAdapter(clinicAdapter);
    }

    private void setPatientAdapter() {
        //TODO el valor de clinicName SUSTITUIR POR CLINICA SELECCIONADA

        patientAdapter = new PatientAdapter(context, patientList, clinicName);
        GridLayoutManager layout = new GridLayoutManager(context, 2);
        recyclerPatients.setLayoutManager(layout);
        recyclerPatients.setAdapter(patientAdapter);
    }

    private void setClinicList() {
        //TODO: Futura Update - Guardar en shared preferences la lista de clinicas que he añadido para filtrar y que solo salgan las mías o un EditText para filtrar en la lista en caso de haber muchas
        firebaseFirestore.collection(CLINICS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen failed.", e);
                    return;
                }

                List<Clinic> list = queryDocumentSnapshots.toObjects(Clinic.class);

                clinicList.addAll(list);
                progressBarDrawer.setVisibility(View.GONE);
                recyclerClinic.setVisibility(View.VISIBLE);
                clinicAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setPatientList() {
        //TODO el valor de clinicName SUSTITUIR POR CLINICA SELECCIONADA

        firebaseFirestore.collection(CLINICS).document(clinicName).collection(PATIENTS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@android.support.annotation.Nullable QuerySnapshot value,
                                @android.support.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen failed.", e);
                    return;
                }

                List<String> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("name") != null) {
                        list.add(doc.getString("name"));
                    }
                }
                patientList.clear();
                patientList.addAll(list);
                progressBarPatients.setVisibility(View.GONE);
                recyclerPatients.setVisibility(View.VISIBLE);
                patientAdapter.notifyDataSetChanged();
                Log.d("INFO", "Current patients in clinic: " + list);
            }
        });
    }

    private void drawerListeners() {
        textAddClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateClinicActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.START);
            }
        });

        textConnectClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.START);

                Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_connect_clinic);
                dialog.setCancelable(false);

                TextView editName = dialog.findViewById(R.id.edit_clinic_name);
                EditText editPassword = dialog.findViewById(R.id.edit_clinic_password);
                ImageView imageCancel = dialog.findViewById(R.id.image_close);
                TextView textSend = dialog.findViewById(R.id.text_send);

                imageCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                textSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clinicName = editName.getText().toString().trim();
                        clinicPassword = editPassword.getText().toString().trim();
                        if (clinicPassword.length() > 0) {
                            if (clinicPassword.equals(clinic.getPassword())) {
                                //TODO REVISAR FORMA DE CONEXION CON LA CLINICA
                                //TODO AÑADIR LOS DATOS DE LA CLINICA A UNA LISTA DE SHARED PREFERENCES PARA MOSTRAR EN EL RECYCLERVIEW
                            } else {
                                editPassword.setError("Datos de inicio de sesion incorrectos");
                                editPassword.requestFocus();
                            }
                        } else {
                            editPassword.setError("El campo no puede estar vacío");
                            editPassword.requestFocus();
                        }
                    }
                });
                dialog.show();
            }
        });

        textLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Configuración", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void patientListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatient();
            }
        });
    }

    private void addPatient() {
        //TODO el valor de clinicName SUSTITUIR POR CLINICA SELECCIONADA

        Intent intent = new Intent(context, AddPatient.class);
        intent.putExtra(CLINIC_NAME, clinicName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}

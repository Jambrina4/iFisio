package com.ojambrina.ifisio.UI.clinics;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.ClinicAdapter;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;

public class ConnectClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_clinic)
    RecyclerView recyclerClinic;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.text_no_clinic)
    TextView textNoClinic;

    List<Clinic> clinicList;
    ClinicAdapter clinicAdapter;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    Context context;
    AppCompatActivity contextForToolbar;
    Clinic getClinic;
    String clinicName;
    String clinicPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_clinic);
        ButterKnife.bind(this);

        context = this;
        contextForToolbar = this;

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
        Utils.configToolbar(contextForToolbar, toolbar);
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
                progressBar.setVisibility(View.GONE);
                recyclerClinic.setVisibility(View.VISIBLE);
                clinicAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(CLINICS);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setAdapter() {
        clinicAdapter = new ClinicAdapter(context, clinicList, new ClinicAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Clinic clinic) {

                clinicName = clinicList.get(position).getName();

                Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_connect_clinic);
                dialog.setCancelable(false);

                TextView textTitle = dialog.findViewById(R.id.text_title);
                EditText editPassword = dialog.findViewById(R.id.edit_password);
                ImageView imageCancel = dialog.findViewById(R.id.image_close);
                TextView textSend = dialog.findViewById(R.id.text_send);

                textTitle.setText(clinicName);
                getClinic = clinic;

                imageCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                textSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clinicPassword = editPassword.getText().toString().trim();
                        if (getClinic != null) {
                            if (clinicPassword.length() > 0) {
                                if (clinicPassword.equals(getClinic.getPassword())) {
                                    Intent intent = new Intent(context, ClinicActivity.class);
                                    intent.putExtra(CLINIC_NAME, clinicName);
                                    dialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    editPassword.setError("Datos de inicio de sesion incorrectos");
                                    editPassword.requestFocus();
                                }
                            } else {
                                editPassword.setError("El campo no puede estar vacío");
                                editPassword.requestFocus();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
        recyclerClinic.setAdapter(clinicAdapter);
    }
}

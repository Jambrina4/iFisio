package com.ojambrina.ifisio.UI.clinics.patients.patientDetail;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.PatientHistoryAdapter;
import com.ojambrina.ifisio.entities.Patient;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;

public class HistoryFragment extends Fragment {

    //Butterknife
    @BindView(R.id.image_patient)
    ImageView imagePatient;
    @BindView(R.id.text_patient_name)
    TextView textPatientName;
    @BindView(R.id.text_born_date)
    TextView textBornDate;
    @BindView(R.id.text_identity_number)
    TextView textIdentityNumber;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_email)
    TextView textEmail;
    @BindView(R.id.text_profession)
    TextView textProfession;
    @BindView(R.id.recycler_medication)
    RecyclerView recyclerMedication;
    @BindView(R.id.recycler_medic_conditions)
    RecyclerView recyclerMedicConditions;
    @BindView(R.id.recycler_regular_exercise)
    RecyclerView recyclerRegularExercise;
    @BindView(R.id.recycler_surgical_operations)
    RecyclerView recyclerSurgicalOperations;
    @BindView(R.id.recycler_medic_examination)
    RecyclerView recyclerMedicExamination;
    @BindView(R.id.image_add_medication)
    ImageView imageAddMedication;
    @BindView(R.id.image_add_medic_condition)
    ImageView imageAddMedicCondition;
    @BindView(R.id.image_add_exercise)
    ImageView imageAddExercise;
    @BindView(R.id.image_add_surgical_operation)
    ImageView imageAddSurgicalOperation;
    @BindView(R.id.image_add_medic_examination)
    ImageView imageAddMedicExamination;

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    private Patient patient;
    private String patientName;
    private String clinicName;
    private PatientHistoryAdapter patientHistoryAdapter;
    private List<String> regularMedication = new ArrayList<>();
    private List<String> medicConditions = new ArrayList<>();
    private List<String> regularExercise = new ArrayList<>();
    private List<String> surgicalOperations = new ArrayList<>();
    private List<String> medicExamination = new ArrayList<>();
    Unbinder unbinder;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            patientName = (String) getArguments().get(PATIENT_NAME);
            clinicName = (String) getArguments().get(CLINIC_NAME);
        }

        context = getContext();

        setFirebase();
        getPatientData();
        listeners();

        return view;
    }

    private void listeners() {
        imageAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        imageAddMedicCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        imageAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

        imageAddSurgicalOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(3);
            }
        });

        imageAddMedicExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(4);
            }
        });
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getPatientData() {
        firebaseFirestore.collection(CLINICS).document(clinicName).collection(PATIENTS).document(patientName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen failed.", e);
                    return;
                }

                patient = documentSnapshot.toObject(Patient.class);

                printData();

            }
        });
        //firebaseFirestore.collection(CLINICS).document(clinicName).collection(PATIENTS).document(patientName).addSnapshotListener(new EventListener<QuerySnapshot>() {
        //    @Override
        //    public void onEvent(@android.support.annotation.Nullable QuerySnapshot value,
        //                        @android.support.annotation.Nullable FirebaseFirestoreException e) {
        //        if (e != null) {
        //            Log.w("ERROR", "Listen failed.", e);
        //            return;
        //        }

        //        List<String> list = new ArrayList<>();
        //        for (QueryDocumentSnapshot doc : value) {
        //            if (doc.get("name") != null) {
        //                list.add(doc.getString("name"));
        //            }
        //        }
        //        //patientList.clear();
        //        //patientList.addAll(list);
        //        //progressBarPatients.setVisibility(View.GONE);
        //        //recyclerPatients.setVisibility(View.VISIBLE);
        //        //patientAdapter.notifyDataSetChanged();
        //        Log.d("INFO", "Current patients in clinic: " + list);
        //    }
        //});
    }

    private void printData() {
        String fullName = patient.getName() + " " + patient.getSurname();
        //TODO CALCULAR LA EDAD A PARTIR DE LA FECHA
        String bornDate = textBornDate.getText() + " " + patient.getBornDate();
        String identityNumber = textIdentityNumber.getText() + " " + patient.getIdentityNumber();
        String phone = textPhone.getText() + " " + patient.getPhone();
        String email = textEmail.getText() + " " + patient.getEmail();
        String profession = textProfession.getText() + " " + patient.getProfession();


        //TODO DESCOMENTAR CUANDO CONSIGA ALMACENAR LA IMAGEN
        //Glide.with(context)
        //        .load(patient.getProfileImage())
        //        .into(imagePatient);

        textPatientName.setText(fullName);
        textBornDate.setText(bornDate);
        textIdentityNumber.setText(identityNumber);
        textPhone.setText(phone);
        textEmail.setText(email);
        textProfession.setText(profession);

        patientHistoryAdapter = new PatientHistoryAdapter(context, patient.getRegularMedication());
        recyclerMedication.setAdapter(patientHistoryAdapter);
        patientHistoryAdapter = new PatientHistoryAdapter(context, patient.getMedicConditions());
        recyclerMedicConditions.setAdapter(patientHistoryAdapter);
        patientHistoryAdapter = new PatientHistoryAdapter(context, patient.getRegularExercise());
        recyclerRegularExercise.setAdapter(patientHistoryAdapter);
        patientHistoryAdapter = new PatientHistoryAdapter(context, patient.getSurgicalOperations());
        recyclerSurgicalOperations.setAdapter(patientHistoryAdapter);
        patientHistoryAdapter = new PatientHistoryAdapter(context, patient.getMedicExamination());
        recyclerMedicExamination.setAdapter(patientHistoryAdapter);
    }

    public void showDialog(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_add_item, null);
        dialogBuilder.setView(dialogView);

        final EditText editItem = dialogView.findViewById(R.id.edit_item);

        switch (position) {
            case 0:
                dialogBuilder.setTitle("Añadir medicación habitual");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            regularMedication.clear();
                            regularMedication.add(editItem.getText().toString().trim());
                            patient.setRegularMedication(regularMedication);
                            firebaseFirestore.collection(CLINICS).document(clinicName).collection(PATIENTS).document(patientName).set(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    patientHistoryAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                break;
            case 1:
                dialogBuilder.setTitle("Añadir afección médica");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            //reasonList.add(editItem.getText().toString().trim());
                            //session.setReasonList(reasonList);
                            //firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //    @Override
                            //    public void onComplete(@NonNull Task<Void> task) {
                            //        sessionReasonAdapter.notifyDataSetChanged();
                            //    }
                            //});
                        }
                    }
                });
                break;
            case 2:
                dialogBuilder.setTitle("Añadir ejercicio habitual");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            //reasonList.add(editItem.getText().toString().trim());
                            //session.setReasonList(reasonList);
                            //firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //    @Override
                            //    public void onComplete(@NonNull Task<Void> task) {
                            //        sessionReasonAdapter.notifyDataSetChanged();
                            //    }
                            //});
                        }
                    }
                });
                break;
            case 3:
                dialogBuilder.setTitle("Añadir operación quirúrjica");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            //reasonList.add(editItem.getText().toString().trim());
                            //session.setReasonList(reasonList);
                            //firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //    @Override
                            //    public void onComplete(@NonNull Task<Void> task) {
                            //        sessionReasonAdapter.notifyDataSetChanged();
                            //    }
                            //});
                        }
                    }
                });
                break;
            case 4:
                dialogBuilder.setTitle("Añadir reconocimiento médico");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            //reasonList.add(editItem.getText().toString().trim());
                            //session.setReasonList(reasonList);
                            //firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //    @Override
                            //    public void onComplete(@NonNull Task<Void> task) {
                            //        sessionReasonAdapter.notifyDataSetChanged();
                            //    }
                            //});
                        }
                    }
                });
                break;


        }

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

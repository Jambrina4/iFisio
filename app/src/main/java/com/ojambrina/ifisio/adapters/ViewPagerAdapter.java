package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.FisioFragment;
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.HistoryFragment;
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.PsychologyFragment;
import com.ojambrina.ifisio.entities.Patient;

import javax.annotation.Nullable;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENT;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String clinic_name;
    private String patientName;
    private Bundle bundle = new Bundle();
    private Patient patient;
    private FirebaseFirestore firebaseFirestore;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context, String clinic_name, String patientName, FirebaseFirestore firebaseFirestore) {
        super(fragmentManager);
        this.context = context;
        this.clinic_name = clinic_name;
        this.patientName = patientName;
        this.firebaseFirestore = firebaseFirestore;
        getData();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        //return 3;
        return 2;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HistoryFragment historyFragment = new HistoryFragment();
                bundle.putSerializable(PATIENT, patient);
                bundle.putSerializable(CLINIC_NAME, clinic_name);
                bundle.putSerializable(PATIENT_NAME, patientName); historyFragment.setArguments(bundle);
                return historyFragment;
            case 1:
                FisioFragment fisioFragment = new FisioFragment();
                bundle.putSerializable(PATIENT, patient);
                bundle.putSerializable(CLINIC_NAME, clinic_name);
                bundle.putSerializable(PATIENT_NAME, patientName); fisioFragment.setArguments(bundle);
                return fisioFragment;
            case 2:
                PsychologyFragment psychologyFragment = new PsychologyFragment();
                bundle.putSerializable(PATIENT, patient);
                bundle.putSerializable(CLINIC_NAME, clinic_name);
                bundle.putSerializable(PATIENT_NAME, patientName);  psychologyFragment.setArguments(bundle);
                return psychologyFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.history);
            case 1:
                return context.getString(R.string.fisiotherapy);
            //case 2:
            //return context.getString(R.string.psychology);
            default:
                return null;
        }
    }

    public void getData() {
        firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen failed.", e);
                    return;
                }

                patient = documentSnapshot.toObject(Patient.class);
            }
        });
    }
}

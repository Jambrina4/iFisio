package com.ojambrina.ifisio.UI.clinics.patients.patientDetail;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.SessionAdapter;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.entities.Session;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENT;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;
import static com.ojambrina.ifisio.utils.Constants.SESSION_LIST;

public class FisioFragment extends Fragment {

    @BindView(R.id.recycler_session)
    RecyclerView recyclerSession;
    @BindView(R.id.add_session)
    FloatingActionButton fab;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private Patient patient;
    private String clinic_name;
    private String patientName;
    private List<Session> sessionList = new ArrayList<>();
    private List<String> reasonList = new ArrayList<>();
    private List<String> explorationList = new ArrayList<>();
    private List<String> treatmentList = new ArrayList<>();
    private SessionAdapter sessionAdapter;
    private HashMap<String, Session> sessionHashMap;
    private Session session;
    private String currentDay;

    Unbinder unbinder;

    public FisioFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fisio, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            patient = (Patient) getArguments().get(PATIENT);
            clinic_name = (String) getArguments().get(CLINIC_NAME);
            patientName = (String) getArguments().get(PATIENT_NAME);
        }

        context = getContext();

        setFirebase();
        getSessionList();
        setAdapter();
        listeners();

        return view;
    }

    private void setAdapter() {
        sessionAdapter = new SessionAdapter(context, session, sessionList, clinic_name, patientName);
        recyclerSession.setAdapter(sessionAdapter);
    }

    private void listeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSession();
                firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(Utils.getCurrentDay()).set(session);
                sessionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getSessionList() {
        firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen failed.", e);
                    return;
                }

                List<Session> list = queryDocumentSnapshots.toObjects(Session.class);

                sessionList.clear();
                sessionList.addAll(list);
                sessionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addSession() {
        session = new Session();
        String sessionDate = Utils.getCurrentDay();
        session.setDate(sessionDate);
        session.setReasonList(reasonList);
        session.setExplorationList(explorationList);
        session.setTreatmentList(treatmentList);
        sessionList.add(session);
    }

    private void setFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("clinicas");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

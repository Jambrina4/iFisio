package com.ojambrina.ifisio.UI.clinics.patients.patientDetail;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.adapters.SessionAdapter;
import com.ojambrina.ifisio.entities.Patient;
import com.ojambrina.ifisio.entities.Session;
import com.ojambrina.ifisio.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ojambrina.ifisio.utils.Constants.PATIENT;

public class FisioFragment extends Fragment {

    @BindView(R.id.recycler_session)
    RecyclerView recyclerSession;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Context context;
    private Patient patient;
    private List<Session> sessionList = new ArrayList<>();
    private SessionAdapter sessionAdapter;
    Unbinder unbinder;

    public FisioFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            patient = (Patient) getArguments().get(PATIENT);
        }

        context = getContext();

        listeners();
        setAdapter();

        View view = inflater.inflate(R.layout.fragment_fisio, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void setAdapter() {
        sessionAdapter = new SessionAdapter(context, sessionList);
        recyclerSession.setAdapter(sessionAdapter);
    }

    private void listeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = new Session();
                sessionList.add(session);
                sessionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

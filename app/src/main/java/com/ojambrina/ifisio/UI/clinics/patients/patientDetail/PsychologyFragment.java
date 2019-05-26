package com.ojambrina.ifisio.UI.clinics.patients.patientDetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Patient;

import static com.ojambrina.ifisio.utils.Constants.PATIENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PsychologyFragment extends Fragment {

    private Patient patient;

    public PsychologyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            patient = (Patient) getArguments().get(PATIENT);
        }
        return inflater.inflate(R.layout.fragment_psychology, container, false);
    }

}

package com.ojambrina.ifisio.UI.clinics.patients.PatientDetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojambrina.ifisio.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FisioFragment extends Fragment {


    public FisioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fisio, container, false);
    }

}

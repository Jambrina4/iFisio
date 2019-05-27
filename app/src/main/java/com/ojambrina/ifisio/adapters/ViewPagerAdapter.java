package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.FisioFragment;
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.HistoryFragment;
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.PsychologyFragment;
import com.ojambrina.ifisio.entities.Patient;

import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENT;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Patient patient;
    private String clinic_name;
    private String patientName;
    private Bundle bundle = new Bundle();

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context, Patient patient, String clinic_name, String patientName) {
        super(fragmentManager);
        this.context = context;
        this.patient = patient;
        this.clinic_name = clinic_name;
        this.patientName = patientName;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 3;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        bundle.putSerializable(PATIENT, patient);
        bundle.putSerializable(CLINIC_NAME, clinic_name);
        bundle.putSerializable(PATIENT_NAME, patientName);
        switch (position) {
            case 0:
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.setArguments(bundle);
                return historyFragment;
            case 1:
                FisioFragment fisioFragment = new FisioFragment();
                fisioFragment.setArguments(bundle);
                return fisioFragment;
            case 2:
                PsychologyFragment psychologyFragment = new PsychologyFragment();
                psychologyFragment.setArguments(bundle);
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
            case 2:
                return context.getString(R.string.psychology);
            default:
                return null;
        }
    }

}

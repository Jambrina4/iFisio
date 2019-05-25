package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.patients.PatientDetail.FisioFragment;
import com.ojambrina.ifisio.UI.clinics.patients.PatientDetail.HistoryFragment;
import com.ojambrina.ifisio.UI.clinics.patients.PatientDetail.PsychologyFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 3;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HistoryFragment();
            case 1:
                return new FisioFragment();
            case 2:
                return new PsychologyFragment();
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

package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Patient;

import java.util.List;

import butterknife.ButterKnife;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    Patient patient;
    Context context;
    List<Patient> patientList;

    public PatientAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        patient = patientList.get(holder.getAdapterPosition());

        //crear metodo loadGlide en utils para cargar imagen de clinica
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(context, itemView);
        }
    }

    public void setData(List<Patient> newPatientList) {
        patientList = newPatientList;
        notifyDataSetChanged();
    }
}

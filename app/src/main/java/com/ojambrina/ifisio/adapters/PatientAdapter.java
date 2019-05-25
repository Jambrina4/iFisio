package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.patients.PatientDetailActivity;
import com.ojambrina.ifisio.entities.Patient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private Patient patient;
    private Context context;
    private List<String> patientList;

    public PatientAdapter(Context context, List<String> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String position = patientList.get(holder.getAdapterPosition());

        holder.textPatient.setText(position);

        //crear metodo loadGlide en utils para cargar imagen del paciente
        holder.layoutPatient.setOnClickListener(v -> {
            Intent intent = new Intent(context, PatientDetailActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_patient)
        ImageView imagePatient;
        @BindView(R.id.text_patient)
        TextView textPatient;
        @BindView(R.id.layout_patient)
        LinearLayout layoutPatient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setData(List<String> newPatientList) {
        patientList = newPatientList;
        notifyDataSetChanged();
    }
}

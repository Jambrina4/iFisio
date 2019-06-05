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
import com.ojambrina.ifisio.UI.clinics.patients.patientDetail.PatientDetailActivity;
import com.ojambrina.ifisio.entities.Patient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;
import static com.ojambrina.ifisio.utils.Constants.PATIENT_NAME;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private Patient patient;
    private Context context;
    private List<String> patientList;
    private String clinic_name;

    public PatientAdapter(Context context, List<String> patientList, String clinic_name) {
        this.context = context;
        this.patientList = patientList;
        this.clinic_name = clinic_name;
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

        //TODO DESCOMENTAR CUANDO CONSIGA ALMACENAR LA IMAGEN
        //Glide.with(context)
        //        .load(patient.getProfileImage())
        //        .into(holder.imagePatient);
        holder.layoutPatient.setOnClickListener(v -> {
            Intent intent = new Intent(context, PatientDetailActivity.class);
            intent.putExtra(PATIENT_NAME, position);
            intent.putExtra(CLINIC_NAME, clinic_name);
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
}

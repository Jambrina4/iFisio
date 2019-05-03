package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Clinic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolder> {

    Clinic clinic;
    Context context;
    List<Clinic> clinicList;

    public ClinicAdapter(Context context, List<Clinic> clinicList) {
        this.context = context;
        this.clinicList = clinicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        clinic = clinicList.get(holder.getAdapterPosition());

        //crear metodo loadGlide en utils para cargar imagen de clinica
        holder.textClinic.setText(clinic.getName());
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_clinic)
        ImageView imageClinic;
        @BindView(R.id.text_clinic)
        TextView textClinic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(context, itemView);
        }
    }
}

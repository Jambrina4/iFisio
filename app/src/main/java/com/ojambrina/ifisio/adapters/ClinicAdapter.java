package com.ojambrina.ifisio.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.ClinicActivity;
import com.ojambrina.ifisio.entities.Clinic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolder> {

    private String position;
    private Context context;
    private List<Clinic> clinicList;
    private Clinic clinic;
    private FirebaseFirestore firebaseFirestore;
    private String clinicName;
    private OnClickListener listener;

    public ClinicAdapter(Context context, List<Clinic> clinicList, OnClickListener listener) {
        this.context = context;
        this.clinicList = clinicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        clinic = clinicList.get(holder.getAdapterPosition());
        clinicName = clinic.getName();
        holder.textClinic.setText(clinicName);

        holder.layoutClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(holder.getAdapterPosition(), clinic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_clinic)
        TextView textClinic;
        @BindView(R.id.layout_clinic)
        LinearLayout layoutClinic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void onClick(int position, Clinic clinic);
    }
}

package com.ojambrina.ifisio.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.clinics.ClinicActivityDetail;
import com.ojambrina.ifisio.entities.Clinic;
import com.ojambrina.ifisio.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.CLINIC_NAME;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolder> {

    private String position;
    private Context context;
    private List<String> clinicList;
    private Dialog dialog;
    private Clinic clinic;
    private FirebaseFirestore firebaseFirestore;
    private String name;
    private String password;


    public ClinicAdapter(Context context, List<String> clinicList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        position = clinicList.get(holder.getAdapterPosition());
        holder.textClinic.setText(position);

        holder.layoutClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirebase();
                getClinic();
                setDialog();
                dialog.show();
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

    private void setDialog() {
        String clinicName = position;

        //TODO: Mostrar un dialogo con EditText para introducir nombre de la clínica y contraeña y compararlos

        //editName = view.findViewById(R.id.edit_name);
        //editPassword = view.findViewById(R.id.edit_password);

        //name = editName.getText().toString().trim();
        //password = editPassword.getText().toString().trim();

        firebaseFirestore.collection(CLINICS).document(clinicName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        clinic = documentSnapshot.toObject(Clinic.class);

                        if (clinic != null) {
                            if (clinic.getName().equals(name) && clinic.getPassword().equals(password)) {
                                Intent intent = new Intent(context, ClinicActivityDetail.class);
                                String clinicName = position;
                                intent.putExtra(CLINIC_NAME, clinicName);
                                context.startActivity(intent);
                            }// else {
                             //    editName.setError("Datos de inicio de sesion incorrectos");
                             //    editPassword.setError("Datos de inicio de sesion incorrectos");
                             //    editName.requestFocus();
                             //}
                        }
                    }
                });
    }

    private void getClinic() {
        firebaseFirestore.collection(CLINICS).document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                clinic = documentSnapshot.toObject(Clinic.class);
            }
        });
    }

    private void setFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CLINICS);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

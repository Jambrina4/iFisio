package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Session;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.SESSION_LIST;

public class SessionReasonAdapter extends RecyclerView.Adapter<SessionReasonAdapter.ViewHolder> {

    private Context context;
    private List<String> reasonList;
    private List<String> highlightList;
    private String reason;
    private FirebaseFirestore firebaseFirestore;
    private String clinic_name;
    private String patientName;
    private String date;
    private Session session;

    public SessionReasonAdapter(Context context, List<String> reasonList, List<String> highlightList, Session session, String clinic_name, String patientName, String date) {
        this.context = context;
        this.reasonList = reasonList;
        this.highlightList = highlightList;
        this.session = session;
        this.clinic_name = clinic_name;
        this.patientName = patientName;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        setFirebase();

        reason = reasonList.get(holder.getAdapterPosition());

        holder.textDetail.setText(reason);

        //holder.layoutHighlight.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        highlightList.add(reasonList.get(holder.getAdapterPosition()));
        //        session.setHighlightList(highlightList);
        //        firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session);
        //    }
        //});

        //TODO ARREGLAR BUG EL ICONO NO SE MUESTRA CORRECTAMENTE AL DARLE A AÑADIR O ELIMINAR DE HIGHLIGHTLIST

        holder.layoutHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highlightList.contains(reasonList.get(holder.getAdapterPosition()))) {
                    highlightList.remove(reasonList.get(holder.getAdapterPosition()));
                    holder.imageHighlight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_stars_black_24dp));
                } else {
                    highlightList.add(reasonList.get(holder.getAdapterPosition()));
                    holder.imageHighlight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_black_24dp));
                }
                session.setHighlightList(highlightList);
                firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session);
            }
        });

        holder.layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonList.remove(reasonList.get(holder.getAdapterPosition()));
                session.setReasonList(reasonList);
                firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_detail)
        ImageView imageDetail;
        @BindView(R.id.text_detail)
        TextView textDetail;
        @BindView(R.id.image_highlight)
        ImageView imageHighlight;
        @BindView(R.id.layout_highlight)
        LinearLayout layoutHighlight;
        @BindView(R.id.image_remove)
        ImageView imageRemove;
        @BindView(R.id.layout_remove)
        LinearLayout layoutRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CLINICS);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

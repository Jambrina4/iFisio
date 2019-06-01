package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Session;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.SESSION_LIST;

public class SessionHighlightAdapter extends RecyclerView.Adapter<SessionHighlightAdapter.ViewHolder> {

    private Context context;
    private Session session;
    private String highlight;
    private List<String> highlightList;
    private String clinic_name;
    private String patientName;
    private String date;
    private FirebaseFirestore firebaseFirestore;

    public SessionHighlightAdapter(Context context, List<String> highlightList, Session session, String clinic_name, String patientName, String date) {
        this.context = context;
        this.highlightList = highlightList;
        this.session = session;
        this.clinic_name = clinic_name;
        this.patientName = patientName;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_higlight_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        setFirebase();

        highlight = highlightList.get(holder.getAdapterPosition());
        holder.textDetail.setText(highlight);

        holder.layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightList.remove(highlightList.get(holder.getAdapterPosition()));
                session.setHighlightList(highlightList);
                firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return highlightList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_detail)
        ImageView imageDetail;
        @BindView(R.id.text_detail)
        TextView textDetail;
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
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

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private Context context;
    private Session session;
    private List<Session> sessionList;
    private FirebaseFirestore firebaseFirestore;

    public SessionAdapter(Context context, List<Session> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        session = sessionList.get(holder.getAdapterPosition());

        holder.textSessionDate.setText(session.getDate());
        holder.layoutSession.setOnClickListener(v -> {
            if (holder.layoutSessionDetail.getVisibility() == View.VISIBLE) {
                holder.layoutSessionDetail.setVisibility(View.GONE);
            } else {
                holder.layoutSessionDetail.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_session_date)
        TextView textSessionDate;
        @BindView(R.id.recycler_session_highligt)
        RecyclerView recyclerSessionHighligt;
        @BindView(R.id.recycler_add_visit_reason)
        RecyclerView recyclerAddVisitReason;
        @BindView(R.id.image_add_reason)
        ImageView imageAddReason;
        @BindView(R.id.recycler_add_exploration)
        RecyclerView recyclerAddExploration;
        @BindView(R.id.image_add_exploration)
        ImageView imageAddExploration;
        @BindView(R.id.recycler_add_treatment)
        RecyclerView recyclerAddTreatment;
        @BindView(R.id.image_add_treatment)
        ImageView imageAddTreatment;
        @BindView(R.id.layout_session_detail)
        LinearLayout layoutSessionDetail;
        @BindView(R.id.layout_session)
        LinearLayout layoutSession;

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
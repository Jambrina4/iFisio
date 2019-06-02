package com.ojambrina.ifisio.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Session;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ojambrina.ifisio.utils.Constants.CLINICS;
import static com.ojambrina.ifisio.utils.Constants.PATIENTS;
import static com.ojambrina.ifisio.utils.Constants.SESSION_LIST;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private Context context;
    private Session session;
    private List<Session> sessionList;
    private FirebaseFirestore firebaseFirestore;
    private List<String> highlightList = new ArrayList<>();
    private List<String> reasonList = new ArrayList<>();
    private List<String> explorationList = new ArrayList<>();
    private List<String> treatmentList = new ArrayList<>();
    private String clinic_name;
    private String patientName;
    private String date;

    private SessionHighlightAdapter sessionHighlightAdapter;
    private SessionReasonAdapter sessionReasonAdapter;
    private SessionExplorationAdapter sessionExplorationAdapter;
    private SessionTreatmentAdapter sessionTreatmentAdapter;

    public SessionAdapter(Context context, Session session, List<Session> sessionList, String clinic_name, String patientName) {
        this.context = context;
        this.session = session;
        this.sessionList = sessionList;
        this.clinic_name = clinic_name;
        this.patientName = patientName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        //TODO Mostrar la lista con las tarjetas nuevas en primera posición
        //TODO arreglar bug que en las tarjetas se muestran los datos de la sesión errónea (Dándole al boton de añadir se muestra correctamente la lista)
        //TODO añadir método para ordenar las listas con session.getDateMillis

        setFirebase();
        session = sessionList.get(holder.getAdapterPosition());

        date = session.getDate();

        setSession(holder);

        holder.textSessionDate.setText(session.getDate());
        holder.layoutSession.setOnClickListener(v -> {
            if (holder.layoutSessionDetail.getVisibility() == View.VISIBLE) {
                holder.layoutSessionDetail.setVisibility(View.GONE);
            } else {
                holder.layoutSessionDetail.setVisibility(View.VISIBLE);
            }
        });

        holder.imageAddReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder, 0);
            }
        });

        holder.imageAddExploration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder, 1);
            }
        });

        holder.imageAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder, 2);
            }
        });
    }

    private void setSession(ViewHolder holder) {
        highlightList.clear();
        reasonList.clear();
        explorationList.clear();
        treatmentList.clear();

        sessionHighlightAdapter = new SessionHighlightAdapter(context, highlightList, session, clinic_name, patientName, date);
        sessionReasonAdapter = new SessionReasonAdapter(context, reasonList, highlightList, session, clinic_name, patientName, date);
        sessionExplorationAdapter = new SessionExplorationAdapter(context, explorationList, highlightList, session, clinic_name, patientName, date);
        sessionTreatmentAdapter = new SessionTreatmentAdapter(context, treatmentList, highlightList, session, clinic_name, patientName, date);

        highlightList.addAll(session.getHighlightList());
        reasonList.addAll(session.getReasonList());
        explorationList.addAll(session.getExplorationList());
        treatmentList.addAll(session.getTreatmentList());

        holder.recyclerSessionHighligt.setAdapter(sessionHighlightAdapter);
        holder.recyclerAddVisitReason.setAdapter(sessionReasonAdapter);
        holder.recyclerAddExploration.setAdapter(sessionExplorationAdapter);
        holder.recyclerAddTreatment.setAdapter(sessionTreatmentAdapter);

        sessionHighlightAdapter.notifyDataSetChanged();
        sessionReasonAdapter.notifyDataSetChanged();
        sessionExplorationAdapter.notifyDataSetChanged();
        sessionTreatmentAdapter.notifyDataSetChanged();
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

    public void showDialog(ViewHolder holder, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.dialog_add_item, null);
        dialogBuilder.setView(dialogView);

        final EditText editItem = dialogView.findViewById(R.id.edit_item);

        date = holder.textSessionDate.getText().toString();

        switch (position) {
            case 0:
                dialogBuilder.setTitle("Añadir motivo de visita");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            reasonList.add(editItem.getText().toString().trim());
                            session.setReasonList(reasonList);
                            firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sessionReasonAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                break;
            case 1:
                dialogBuilder.setTitle("Añadir exploración");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            explorationList.add(editItem.getText().toString().trim());
                            session.setExplorationList(explorationList);
                            firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sessionExplorationAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                break;
            case 2:
                dialogBuilder.setTitle("Añadir tratamiento");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editItem.getText().toString().trim().length() != 0) {
                            treatmentList.add(editItem.getText().toString().trim());
                            session.setTreatmentList(treatmentList);
                            firebaseFirestore.collection(CLINICS).document(clinic_name).collection(PATIENTS).document(patientName).collection(SESSION_LIST).document(date).set(session).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sessionTreatmentAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                break;
        }

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
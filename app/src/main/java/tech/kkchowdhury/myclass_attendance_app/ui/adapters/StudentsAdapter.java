package tech.kkchowdhury.myclass_attendance_app.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;
import tech.kkchowdhury.myclass_attendance_app.response.ShowGraph;
import tech.kkchowdhury.myclass_attendance_app.response.ShowPersonalDetails;
import tech.kkchowdhury.myclass_attendance_app.model.studentlist_activity;
import tech.kkchowdhury.myclass_attendance_app.ui.activities.MainActivity;


public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsHolder> {
    Context context;
    List<studentlist_activity> studentsList;

    private static final String dayAPI = ApiUrls.MARK_PRESENT_API;

    String dayVal = MainActivity.rtnDay();
    String monVal = MainActivity.rtnMon();
    String yearVal = MainActivity.rtnYear();
    String courseid = MainActivity.rtnCourse();


    public StudentsAdapter(Context context, List<studentlist_activity> studentsList) {
        this.context = context;
        this.studentsList = studentsList;
    }

    @NonNull
    @Override
    public StudentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View studentLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list,parent,false);
        return new StudentsHolder(studentLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsHolder holder, int position) {
        studentlist_activity students = studentsList.get(position);

        final studentlist_activity temp = studentsList.get(position);

        holder.sl.setText(students.getSl());
        holder.name.setText(students.getName());
        holder.roll.setText(students.getRoll());
        if (students.getStatus().equals("present")){
            holder.status.setText(students.getStatus());
            holder.status.setTextColor(Color.GREEN);
        }else {
            holder.status.setText(students.getStatus());
            holder.status.setTextColor(Color.RED);
        }

        Glide.with(context).load(studentsList.get(position).getImageurl()).into(holder.imageView);

        holder.materialCardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_type_one));

        holder.showDtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowPersonalDetails.class);
                intent.putExtra("nameval", temp.getName());
                intent.putExtra("rollval", temp.getRoll());
//                intent.putExtra("imageval", temp.getImageurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.showGph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, ShowGraph.class);
                intent1.putExtra("rollval", temp.getRoll());
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        });

        holder.markPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("MARK PRESENT-> "+students.getName());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String rollno = students.getRoll().trim();
                        String date = yearVal+"-"+monVal+"-"+dayVal;
                        Toast.makeText(context, dayVal, Toast.LENGTH_SHORT).show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, dayAPI, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equals("present")){
                                    int position = holder.getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        removeItem(position);
                                    }
                                }

                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){

                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> params = new HashMap<>();
                                params.put("dateVal",date);
                                params.put("roll",rollno);
                                params.put("courseid", courseid);
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(stringRequest);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<studentlist_activity> filteredList){
        studentsList = filteredList;
        notifyDataSetChanged();
    }

    // remove after present

    public void removeItem(int position) {
        studentsList.remove(position);
        notifyItemRemoved(position);
    }


    // can be made non static later
    public static class StudentsHolder extends RecyclerView.ViewHolder{

        TextView sl,name,roll,status,showDtl,showGph;
        Button markPresent;

        ImageView imageView;

        MaterialCardView materialCardView;

        public StudentsHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.cardView);
            sl = itemView.findViewById(R.id.rcy_slno);
            name = itemView.findViewById(R.id.rcy_name);
            roll= itemView.findViewById(R.id.rcy_roll);
            markPresent = itemView.findViewById(R.id.btnPresent);
            status = itemView.findViewById(R.id.tvStatus);
            imageView = itemView.findViewById(R.id.imageView);
            showDtl = itemView.findViewById(R.id.showDetailsTV);
            showGph = itemView.findViewById(R.id.showGraphTV);
        }
    }
}

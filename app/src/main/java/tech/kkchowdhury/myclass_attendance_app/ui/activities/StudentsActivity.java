package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;
import tech.kkchowdhury.myclass_attendance_app.model.studentlist_activity;
import tech.kkchowdhury.myclass_attendance_app.ui.adapters.StudentsAdapter;

public class StudentsActivity extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private StudentsAdapter studentsAdapter;


    private List<studentlist_activity> studentList;

    String dval = MainActivity.rtnDay();
    String mval = MainActivity.rtnMon();
    String yval = MainActivity.rtnYear();
    String deptval = MainActivity.rtnDept();
    String semval = MainActivity.rtnSem();
    String courseval = MainActivity.rtnCourse();

    String selectedDate = yval+"-"+mval+"-"+dval;
    private final String apiurl_dval = ApiUrls.FETCH_STUDENTS_API1 + "?t1=" + selectedDate + "&t2=" + deptval + "&t3=" + semval + "&t4=" + courseval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        recyclerView = findViewById(R.id.recyclerList);
        shimmerFrameLayout = findViewById(R.id.student_shimmer);
        shimmerFrameLayout.startShimmer();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.search_id);
        studentList = new ArrayList<>();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Showing " + deptval + ": SEM "+semval+" students");
        LoadAllStudents();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<studentlist_activity> filteredList = new ArrayList<>();

        for (studentlist_activity item : studentList){
            if(item.getName().toLowerCase().startsWith(newText.toLowerCase())){
                filteredList.add(item);
            }
        }

//        studentsAdapter = new StudentsAdapter(StudentsActivity.this,studentList);
        studentsAdapter.filterList(filteredList);
    }

    private void LoadAllStudents(){

        JsonArrayRequest request = new JsonArrayRequest(apiurl_dval, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                for (int i=0; i<array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);

                        String name = object.getString("name").trim();
                        String roll = object.getString("roll").trim();
                        String tempDate = object.getString("date").trim();
                        String status = object.getString("ap").trim();
                        String imageurl = object.getString("imageurl").trim();
                        System.out.println("Status: " + status);

                        String imgurl = ApiUrls.FETCH_STUDENTS_API2+imageurl;

                        studentlist_activity students = new studentlist_activity();
                        if(tempDate.equals("null")){
                            students.setSl(selectedDate);
                        }else if(!roll.equals("")){
                            students.setSl(tempDate);
                        }else{
                            recyclerView.setVisibility(View.GONE);
                        }

                        students.setName(name);
                        students.setRoll(roll);

                        students.setImageurl(imgurl);
//                        students.setStatus(status);
                        if(status.equals("1")){
                            students.setStatus("present");
                        }else {
                            students.setStatus("absent");
                        }
                        studentList.add(students);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                studentsAdapter = new StudentsAdapter(StudentsActivity.this,studentList);
                recyclerView.setAdapter(studentsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(StudentsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(StudentsActivity.this);
        requestQueue.add(request);

    }

}
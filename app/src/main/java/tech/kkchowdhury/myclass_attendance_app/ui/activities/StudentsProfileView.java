package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;

public class StudentsProfileView extends AppCompatActivity {

    ImageView proPic;
    TextView sLoc, sName, sRoll, sSem, sMobile, sGnumber;
    EditText getRollEt;
    AppCompatButton searchButton;

    ShimmerFrameLayout studentSearchShimmer;
    LinearLayout baseLayoutStudentSearching;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_profile_view);

        studentSearchShimmer = findViewById(R.id.shimmer_searching_student);
        baseLayoutStudentSearching = findViewById(R.id.base_layout_searching);

        studentSearchShimmer.setVisibility(View.INVISIBLE);
        baseLayoutStudentSearching.setVisibility(View.INVISIBLE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);



        proPic = findViewById(R.id.imageViewProfile);
        sName = findViewById(R.id.textViewName);
        sRoll = findViewById(R.id.textViewRoll);
        sSem = findViewById(R.id.textViewSem);
        sMobile = findViewById(R.id.textViewMobile);
        sLoc = findViewById(R.id.textViewLocation);
        sGnumber = findViewById(R.id.textViewGnumber);
        getRollEt = findViewById(R.id.editTextRollNumber);
        searchButton = findViewById(R.id.buttonSearch);



        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_search:
                    startActivity(new Intent(getApplicationContext(), DatePickerActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_settings:
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), FacultyPersonalProfile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                studentSearchShimmer.setVisibility(View.VISIBLE);
                studentSearchShimmer.startShimmer();
                Handler handler = new Handler();
                handler.postDelayed(()->{
                    baseLayoutStudentSearching.setVisibility(View.VISIBLE);
                    studentSearchShimmer.stopShimmer();
                    studentSearchShimmer.setVisibility(View.INVISIBLE);
                },3000);

                String globalfilterDetailsAPI = ApiUrls.STUDENTS_PROFILE_SEARCH_API1 + "?t1=" + getRollEt.getText().toString().trim();

                LoadStudentProfile(globalfilterDetailsAPI);


            }
        });

    }

    private void LoadStudentProfile(String apiURL) {

        JsonArrayRequest request = new JsonArrayRequest(apiURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {

                for (int i=0; i<array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);

                        String imageurl = object.getString("imageurl").trim();
                        String name = object.getString("name").trim();
                        String roll = object.getString("roll").trim();
                        String sem = object.getString("sem").trim();
                        String mobile = object.getString("s_mobile").trim();
                        String loc = object.getString("s_location").trim();
                        String gmobile = object.getString("s_gmobile").trim();

//                        System.out.println(gmobile);
                        Toast.makeText(StudentsProfileView.this, gmobile, Toast.LENGTH_SHORT).show();

                        String imgurl = ApiUrls.STUDENTS_PROFILE_SEARCH_API2+imageurl;

                        Glide.with(StudentsProfileView.this)
                                .load(imgurl)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)) // Cache the image for better performance
                                .into(proPic);
                        sName.setText(name);
                        sRoll.setText(roll);
                        sSem.setText(sem);
                        sMobile.setText(mobile);
                        sLoc.setText(loc);
                        sGnumber.setText(gmobile);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(StudentsProfileView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(StudentsProfileView.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {

        SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
        if (sp.contains("username")) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
    }
}
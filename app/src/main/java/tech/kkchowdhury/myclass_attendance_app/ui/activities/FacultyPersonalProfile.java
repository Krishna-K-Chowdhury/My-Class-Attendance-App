package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;

public class FacultyPersonalProfile extends AppCompatActivity {

    ImageView imageView;
    TextView textView1, textView2, textView3, textView4, textView5;
    AppCompatButton signOutBtn;

    private String  globalFacultyInfoAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_personal_profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        imageView = findViewById(R.id.imageView2);
        textView1 = findViewById(R.id.textView5);
        textView2 = findViewById(R.id.textView9_1);
        textView3 = findViewById(R.id.textView10_1); // fetch from backend
        textView4 =findViewById(R.id.textView11_1);
        textView5 = findViewById(R.id.textView12_1);

        signOutBtn = findViewById(R.id.signOutid);


        Context context = getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        globalFacultyInfoAPI = ApiUrls.FACULTY_PROFILE_API1 + "?t1=" + username;


        Toast.makeText(context, username, Toast.LENGTH_SHORT).show();

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
                    startActivity(new Intent(getApplicationContext(), StudentsProfileView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    return true;
            }
            return false;
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
                sp.edit().remove("username").commit();
                sp.edit().remove("password").commit();
                sp.edit().apply();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        LoadFacultyDetails();

    }

    private void LoadFacultyDetails(){

        JsonArrayRequest request = new JsonArrayRequest(globalFacultyInfoAPI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {

                for (int i=0; i<array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);

                        String imageurl = object.getString("f_imageurl").trim();
                        String fname = object.getString("f_name").trim();
                        String fdept = object.getString("f_dept").trim();
                        String fmobile = object.getString("f_mobile").trim();
                        String femail = object.getString("f_email").trim();
                        String fcourseid = object.getString("courseid").trim();

                        String imgurl = ApiUrls.FACULTY_PROFILE_API2+imageurl;

                        Glide.with(FacultyPersonalProfile.this)
                                .load(imgurl)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)) // Cache the image for better performance
                                .into(imageView);
                        textView1.setText(fname);
                        textView2.setText(fdept);
                        textView3.setText(femail);
                        textView4.setText(fmobile);
                        textView5.setText(fcourseid);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(FacultyPersonalProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(FacultyPersonalProfile.this);
        requestQueue.add(request);

    }


    @Override
    public void onBackPressed() {

//            super.onBackPressed();
//            finish();
            SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
            if (sp.contains("username")) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
            else{
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
    }

}
package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;

public class RegisterActivity extends AppCompatActivity {

    EditText nameid, rollid, yearid, deptid, s_mobile, s_email, s_location, s_dob, s_gname, s_gmobile;

    Button submitBt, dashBt, redirCourseRegPage;

    boolean isAllFieldsChecked = false;

    public static String carryRoll = "";

    private static final String url= ApiUrls.NEW_STUDENT_REGISTER_API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameid=findViewById(R.id.nameid);
        rollid=findViewById(R.id.rollid);
        yearid=findViewById(R.id.yearid);
        deptid=findViewById(R.id.dept);

        s_mobile=findViewById(R.id.s_mobile);
        s_location=findViewById(R.id.s_location);
        s_email=findViewById(R.id.s_email);
        s_dob=findViewById(R.id.s_dob);
        s_gname=findViewById(R.id.s_gname);
        s_gmobile=findViewById(R.id.s_gmobile);


        submitBt=findViewById(R.id.register);
        dashBt=findViewById(R.id.dashBd);
        redirCourseRegPage=findViewById(R.id.courseRegRedir);

        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked) {

                    carryRoll = rollid.getText().toString();

                    register_user(nameid.getText().toString(), rollid.getText().toString(), yearid.getText().toString(), deptid.getText().toString(), s_mobile.getText().toString(), s_email.getText().toString(), s_location.getText().toString(), s_dob.getText().toString(), s_gname.getText().toString(), s_gmobile.getText().toString());
                }
            }
        });



        dashBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        redirCourseRegPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CourseRegActivity.class));

            }
        });

    }


    public void register_user(final String name, final String roll, final String year, final String dept, final String s_mobile, final String s_email, final String s_location, final String s_dob, final String s_gname, final String s_gmobile){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                if (response.equals("success")){
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(),UploadProfilePic.class));
                    Intent intent = new Intent(RegisterActivity.this, UploadProfilePic.class);
                    intent.putExtra("EXTRA_MESSAGE", carryRoll);
                    startActivity(intent);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("name",name);
                map.put("roll",roll);
                map.put("year",year);
                map.put("dept",dept);
                map.put("s_mobile",s_mobile);
                map.put("s_email",s_email);
                map.put("s_location",s_location);
                map.put("s_dob",s_dob);
                map.put("s_gname",s_gname);
                map.put("s_gmobile",s_gmobile);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    private boolean CheckAllFields(){
        if(nameid.length()==0){
            nameid.setError("Name is required!");
            return false;
        }if(rollid.length()==0){
            rollid.setError("Roll no is required!");
            return false;
        }if(yearid.length()==0){
            yearid.setError("Year required!");
            return false;
        }if(deptid.length()==0){
            deptid.setError("Department required!");
            return false;
        }
        return true;
    }


}
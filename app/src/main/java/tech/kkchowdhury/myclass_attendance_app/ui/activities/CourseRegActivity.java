package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

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

public class CourseRegActivity extends AppCompatActivity {


    String[] courseValues =  {"CSE101","CSE102","CSE103","EE101","EE102","EE103","ME101","ME102","ME103","TT101","TT102","TT103"};

    AutoCompleteTextView autoCompleteCourseReg;
    EditText editTextRoll;
    ArrayAdapter<String> adapterCourseReg;
    public static String courseRegVal = "";

    AppCompatButton regCourseBt;

    private static final String course_reg_url= ApiUrls.COURSE_REG_API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_reg);
        autoCompleteCourseReg = findViewById(R.id.auto_course_field_reg);
        editTextRoll = findViewById(R.id.course_rollid);
        regCourseBt = findViewById(R.id.courseRegRedir);

        adapterCourseReg = new ArrayAdapter<String>(this,R.layout.list_item,courseValues);
        autoCompleteCourseReg.setAdapter(adapterCourseReg);
        autoCompleteCourseReg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                courseRegVal = item;

            }
        });

        regCourseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),courseRegVal +"+"+ editTextRoll.getText().toString(),Toast.LENGTH_SHORT).show();
                register_course(editTextRoll.getText().toString(), courseRegVal);
            }
        });

    }

    public void register_course(final String roll, final String courseid){
        StringRequest request = new StringRequest(Request.Method.POST, course_reg_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("success")){
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    editTextRoll.setText("");
                }else {
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                editTextRoll.setText("");
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("roll",roll);
                map.put("courseid",courseid);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
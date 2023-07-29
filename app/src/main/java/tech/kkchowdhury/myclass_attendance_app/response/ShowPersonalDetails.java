package tech.kkchowdhury.myclass_attendance_app.response;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;

public class ShowPersonalDetails extends AppCompatActivity {

    ImageView imageView;
    TextView textView1, textView2, textView3;


    String x;

    private String  globalfetchDetailsAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_personal_details);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Showing " + getIntent().getStringExtra("nameval") + "'s Details");


        imageView = findViewById(R.id.imageView2);
        textView1 = findViewById(R.id.textView6);
        textView2 = findViewById(R.id.textView7);
        textView3 = findViewById(R.id.textView8); // fetch from backend


        x = getIntent().getStringExtra("rollval");
        globalfetchDetailsAPI = ApiUrls.SHOW_PERSONAL_DETAILS_API1 + x;

        LoadStudentDetails();

        textView1.setText(getIntent().getStringExtra("nameval"));
        textView2.setText(getIntent().getStringExtra("rollval"));


    }

    private void LoadStudentDetails(){

        JsonArrayRequest request = new JsonArrayRequest(globalfetchDetailsAPI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {

                for (int i=0; i<array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);

                        String imageurl = object.getString("imageurl").trim();
                        String semvalue = object.getString("sem").trim();

                        String imgurl = ApiUrls.SHOW_PERSONAL_DETAILS_API2+imageurl;

                        Glide.with(ShowPersonalDetails.this)
                                .load(imgurl)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)) // Cache the image for better performance
                                .into(imageView);
                        textView3.setText(semvalue);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ShowPersonalDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ShowPersonalDetails.this);
        requestQueue.add(request);

    }

}
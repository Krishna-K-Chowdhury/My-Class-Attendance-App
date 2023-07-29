package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.controller;
import tech.kkchowdhury.myclass_attendance_app.model.responsemodel;

public class LoginActivity extends AppCompatActivity {
    EditText t1,t2;
    Button loginbtn;
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        t1= findViewById(R.id.t1);
        t2= findViewById(R.id.t2);
        tv= findViewById(R.id.tv);
        loginbtn= findViewById(R.id.savebtn);
        imageView = findViewById(R.id.login_gif);
        Glide.with(this).asGif().load(R.raw.logingif).into(imageView);

        checkuserexistence();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processlogin();
            }
        });

    }

    void processlogin()
    {
        String email=t1.getText().toString();
        String password=t2.getText().toString();

        Call<responsemodel> call= controller
                .getInstance()
                .getapi()
                .verifyuser(email,password);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                responsemodel obj=response.body();
                String output=obj.getMessage();
                if(output.equals("failed"))
                {
                    t1.setText("");
                    t2.setText("");
                    tv.setTextColor(Color.RED);
                    tv.setText("Invalid username or password");
                }
                if(output.equals("exist"))
                {
                    SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("username",t1.getText().toString());
                    editor.putString("password",t2.getText().toString());
                    editor.commit();
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                tv.setText(t.toString());
                tv.setTextColor(Color.RED);
            }
        });

    }

    void checkuserexistence() {
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("username")) {
            Toast.makeText(LoginActivity.this, "Already Logged in!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else {
            tv.setText("Please login");
        }
    }
}

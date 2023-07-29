package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import tech.kkchowdhury.myclass_attendance_app.R;

public class DatePickerActivity extends AppCompatActivity {

    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private Calendar calendarStartDate;
    private Calendar calendarEndDate;
    private SimpleDateFormat dateFormat;

    AutoCompleteTextView autoCompleteCourse;
    ArrayAdapter<String> adapterCourse;

    String[] courseValues =  {"CSE101","CSE102","CSE103","EE101","EE102","EE103","ME101","ME102","ME103","TT101","TT102","TT103"};

    public static String carryCourseVal = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        Button btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        Button btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        autoCompleteCourse = findViewById(R.id.auto_course_field);

        calendarStartDate = Calendar.getInstance();
        calendarEndDate = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_search:
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), StudentsProfileView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), FacultyPersonalProfile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        btnSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        btnSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        adapterCourse = new ArrayAdapter<String>(this,R.layout.list_item,courseValues);
        autoCompleteCourse.setAdapter(adapterCourse);
        autoCompleteCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                carryCourseVal = item;
                Toast.makeText(getApplicationContext(),"Course: "+item,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                startDateListener,
                calendarStartDate.get(Calendar.YEAR),
                calendarStartDate.get(Calendar.MONTH),
                calendarStartDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void showEndDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                endDateListener,
                calendarEndDate.get(Calendar.YEAR),
                calendarEndDate.get(Calendar.MONTH),
                calendarEndDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }



    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendarStartDate.set(year, month, dayOfMonth);
            textViewStartDate.setText(dateFormat.format(calendarStartDate.getTime()));
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendarEndDate.set(year, month, dayOfMonth);
            textViewEndDate.setText(dateFormat.format(calendarEndDate.getTime()));
        }
    };

    public void saveDates(View view) {
        String startDate = dateFormat.format(calendarStartDate.getTime());
        String endDate = dateFormat.format(calendarEndDate.getTime());

        Intent intent = new Intent(this, DownloadActivity.class);

        intent.putExtra("start_date", startDate);
        intent.putExtra("end_date", endDate);
        intent.putExtra("show_course", carryCourseVal);

        Toast.makeText(DatePickerActivity.this, "Download Started!", Toast.LENGTH_SHORT).show();
        startActivity(intent);

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

    }


}

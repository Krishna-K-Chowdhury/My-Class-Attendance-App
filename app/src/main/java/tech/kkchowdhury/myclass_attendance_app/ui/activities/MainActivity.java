package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import tech.kkchowdhury.myclass_attendance_app.R;

public class MainActivity extends AppCompatActivity {

    String[] deptValues =  {"CSE","EE","ME","TT"};
    String[] semValues =  {"1","2","3","4","5","6","7","8"};
    String[] monParameter =  {"01","02","03","04","05","06","07","08", "09", "10", "11", "12"};
    String[] courseValues =  {"CSE101","CSE102","CSE103","EE101","EE102","EE103","ME101","ME102","ME103","TT101","TT102","TT103"};

    AutoCompleteTextView autoCompleteDept, autoCompleteSem, autoCompleteCourse;
    ArrayAdapter<String> adapterDept, adapterSem, adapterCourse;
    Button downloadBtn, registerBtn, deleteAttnDataBtn;
    public static String text = "";
    public static String text1 = "";
    public static String text2 = "";
    public static String text3 = "";
    public static String semVal = "";
    public static String courseVal = "";

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private long pressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ShimmerFrameLayout shimmerFrameLayoutHome = findViewById(R.id.home_content_shimmer);
        shimmerFrameLayoutHome.startShimmer();

        ConstraintLayout constraintLayoutMainContent = findViewById(R.id.main_content_id);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);


        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);

        autoCompleteDept = findViewById(R.id.auto_dept_field);
        autoCompleteSem = findViewById(R.id.auto_sem_field);
        autoCompleteCourse = findViewById(R.id.auto_course_field);

        downloadBtn= findViewById(R.id.buttonDownload);
        registerBtn= findViewById(R.id.buttonReg);
        deleteAttnDataBtn= findViewById(R.id.btnDeleteAttendance);


        checkuserexistence();
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayoutHome.stopShimmer();
                shimmerFrameLayoutHome.setVisibility(View.GONE);
                constraintLayoutMainContent.setVisibility(View.VISIBLE);
            }
        };

        handler.postDelayed(runnable, 1500);
        dateButton.setText(getTodaysDate());


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
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
                    startActivity(new Intent(getApplicationContext(), FacultyPersonalProfile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });


        adapterDept = new ArrayAdapter<String>(this,R.layout.list_item,deptValues);
        autoCompleteDept.setAdapter(adapterDept);
        autoCompleteDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                text3 = item;

            }
        });
        adapterSem = new ArrayAdapter<String>(this,R.layout.list_item,semValues);
        autoCompleteSem.setAdapter(adapterSem);
        autoCompleteSem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                semVal = item;

            }
        });
        adapterCourse = new ArrayAdapter<String>(this,R.layout.list_item,courseValues);
        autoCompleteCourse.setAdapter(adapterCourse);
        autoCompleteCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                courseVal = item;

            }
        });




        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DatePickerActivity.class));
            }
        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });



        deleteAttnDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DeletionConsentActivity.class));
                Toast.makeText(MainActivity.this, "Warning! Download Data Before Proceeding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkuserexistence() {
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(!(sp.contains("username"))) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                text = String.valueOf(day);
                text1 = String.valueOf(month);
                text2 = String.valueOf(year);
                String date = makeDateString(day, month, year);

                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }


    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";


        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void showStudents(View view) {
        Intent intent = new Intent(MainActivity.this,StudentsActivity.class);

        startActivity(intent);
    }



    public static String rtnDay(){
        switch (text) {
            case "1":
                return "01";
            case "2":
                return "02";
            case "3":
                return "03";
            case "4":
                return "04";
            case "5":
                return "05";
            case "6":
                return "06";
            case "7":
                return "07";
            case "8":
                return "08";
            case "9":
                return "09";
            default:
                return text;
        }
    }
    public static String rtnMon(){
        switch (text1) {
            case "1":
                return "01";
            case "2":
                return "02";
            case "3":
                return "03";
            case "4":
                return "04";
            case "5":
                return "05";
            case "6":
                return "06";
            case "7":
                return "07";
            case "8":
                return "08";
            case "9":
                return "09";
            default:
                return text1;
        }
    }
    public static String rtnYear(){
        return text2;
    }
    public static String rtnDept(){
        return text3;
    }
    public static String rtnSem(){
        return semVal;
    }
    public static String rtnCourse(){
        return courseVal;
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {

            SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
            if (sp.contains("username")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }

        }
        else{
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}
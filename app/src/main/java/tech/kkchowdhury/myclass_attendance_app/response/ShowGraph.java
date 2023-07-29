package tech.kkchowdhury.myclass_attendance_app.response;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;
import tech.kkchowdhury.myclass_attendance_app.ui.utils.WholeNumberValueFormatter;

public class ShowGraph extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout2;

    BarChart bar;
    String  y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);
        bar = findViewById(R.id.barchart);
        shimmerFrameLayout2 = findViewById(R.id.graph_shimmer);
        shimmerFrameLayout2.startShimmer();
        y = getIntent().getStringExtra("rollval");
        Toast.makeText(this, y, Toast.LENGTH_LONG).show();

        String globalGraphAPI = ApiUrls.SHOW_GRAPH + y;

        ArrayList<String> labelsNames = new ArrayList<>();

        labelsNames.add(new String("Demo"));
        labelsNames.add(new String("Jan"));
        labelsNames.add(new String("Feb"));
        labelsNames.add(new String("Mar"));
        labelsNames.add(new String("Apr"));
        labelsNames.add(new String("May"));
        labelsNames.add(new String("Jun"));
        labelsNames.add(new String("Jul"));
        labelsNames.add(new String("Aug"));
        labelsNames.add(new String("Sep"));
        labelsNames.add(new String("Oct"));
        labelsNames.add(new String("Nov"));
        labelsNames.add(new String("Dec"));

        ArrayList<BarEntry> information=new ArrayList<>();


        JsonArrayRequest request = new JsonArrayRequest(globalGraphAPI, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray array) {

                for (int i=0; i<array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);


                        information.add(new BarEntry(object.getInt("month"), object.getInt("total_attendance")));


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
//                bar.notifyDataSetChanged();
                BarDataSet dataset = new BarDataSet(information, "Graphical Report");
                dataset.setColors(ColorTemplate.MATERIAL_COLORS);
                dataset.setValueTextColor(Color.BLACK);
                dataset.setValueTextSize(15f);

                BarData barData = new BarData(dataset);
                barData.setValueFormatter(new WholeNumberValueFormatter());
                barData.setBarWidth(0.8f);
                bar.setFitBars(true);
                bar.setData(barData);
                bar.getDescription().setText("Yearly Attendance Trends");
                bar.animateY(2000);

                XAxis xAxis = bar.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(true);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelsNames.size());
                xAxis.setLabelRotationAngle(270);

                // Notify the bar chart that the data has changed
                bar.notifyDataSetChanged();
                bar.invalidate();
                shimmerFrameLayout2.stopShimmer();
                shimmerFrameLayout2.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);

            }



        }, error -> Toast.makeText(ShowGraph.this, error.toString(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(ShowGraph.this);
        requestQueue.add(request);

    }

}


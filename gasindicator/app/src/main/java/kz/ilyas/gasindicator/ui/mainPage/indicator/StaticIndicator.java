package kz.ilyas.gasindicator.ui.mainPage.indicator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.broooapps.graphview.models.GraphData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.data.model.Statistics;

public class StaticIndicator extends AppCompatActivity {

    private EditText dateStart, dateEnd;
    private TextView id, address, position;
    private Button show;
    private Indicator indicator;
    private StaticIndicatorViewModel staticIndicatorViewModel;
    private ArrayList<Statistics> curStatics;
    private GraphView curveGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_indicator);

        Bundle arguments = getIntent().getExtras();

        curveGraphView = findViewById(R.id.staticIndicator_graphView);
        dateEnd = findViewById(R.id.staticIndicator_dateEnd_value);
        dateStart = findViewById(R.id.staticIndicator_dateStart_value);
        show = findViewById(R.id.staticIndicator_show_button);
        id = findViewById(R.id.staticIndicator_id_value);
        address = findViewById(R.id.staticIndicator_address_value);
        position = findViewById(R.id.staticIndicator_position_value);

        curStatics = new ArrayList<>();

        if (arguments.get("indicator") != null) {
            indicator = (Indicator) arguments.get("indicator");
            id.setText(indicator.getId());
            address.setText(indicator.getAddress());
            position.setText(indicator.getPosition());
        } else {
            finish();
        }

        staticIndicatorViewModel = ViewModelProviders.of(this).get(StaticIndicatorViewModel.class);

        staticIndicatorViewModel.getStatistisc().observe(this, new Observer<ArrayList<Statistics>>() {
            @Override
            public void onChanged(ArrayList<Statistics> statistics) {
                if (statistics != null) {
                    curStatics = staticIndicatorViewModel.sort(statistics);

                    showGraph(curStatics);
                }

                Toast.makeText(StaticIndicator.this, curStatics.size() + "", Toast.LENGTH_SHORT).show();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateEnd.getText().toString().isEmpty() && dateStart.getText().toString().isEmpty()) {
                    Toast.makeText(StaticIndicator.this, "Необходимы выбрать даты", Toast.LENGTH_SHORT).show();
                } else {
                    if (curStatics.size() > 0) {
                        showGraph(curStatics);
                    } else {
                        staticIndicatorViewModel.getStatistics(indicator.getId());
                    }
                }
            }
        });
    }

    private void showGraph(ArrayList<Statistics> newStat) {
        curveGraphView.getViewport().setScalable(true);
        curveGraphView.getViewport().setScrollable(true);
        curveGraphView.getViewport().setScalableY(true);
        curveGraphView.getViewport().setScrollableY(true);

        Statistics oneDay = newStat.get(0);
        DataPoint[] points = new DataPoint[oneDay.getStatics().size() - 1];
        int year = Integer.valueOf(oneDay.getDate().substring(0,4));
        int month = Integer.valueOf(oneDay.getDate().substring(5,7));
        int day = Integer.valueOf(oneDay.getDate().substring(8,10));
        for (int i = 0; i < points.length; i++) {
            int hh = Integer.valueOf(oneDay.getStatics().get(i).getTime().substring(0,2));
            int mm = Integer.valueOf(oneDay.getStatics().get(i).getTime().substring(3,5));
            int ss = Integer.valueOf(oneDay.getStatics().get(i).getTime().substring(6,8));
            points[i] = new DataPoint(i, oneDay.getStatics().get(i).getValueDouble());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setColor(R.color.yellow);
        series.setDrawDataPoints(true);
        curveGraphView.addSeries(series);

//        curveGraphView.configure(
//                new CurveGraphConfig.Builder(this)
//                        .setAxisColor(R.color.yellow)                                             // Set X and Y axis line color stroke.
//                        .setIntervalDisplayCount(10)                                             // Set number of values to be displayed in X ax                         // Set number of background guidelines to be shown.
//                        .setGuidelineColor(R.color.GreenYellow)                                 // Set color of the visible guidelines.
//                        .setNoDataMsg(" No Data ")// Message when no data is provided to the view.
//                        .setxAxisScaleTextColor(R.color.Black)                                  // Set X axis scale text color.
//                        .setyAxisScaleTextColor(R.color.Black)                                  // Set Y axis scale text color
//                        .setAnimationDuration(5000)                                             // Set animation duration to be used after set data.
//                        .build()
//        );
//
//        PointMap pointMap = new PointMap();
//
//        Statistics oneDay = newStat.get(0);

//        for (Statistic stat : oneDay.getStatics()) {
//            pointMap.addPoint(pointCount++, stat.getValueDouble().intValue());
//            Log.v("POINTMAP", pointCount + " " + stat.getValueDouble().intValue());
//        }
////        for (Statistics cur : newStat) {
////            for (Statistic stat : cur.getStatics()) {
////                pointMap.addPoint(pointCount++, Double.valueOf(stat.getValue()).intValue());
////            }
//
//        gd = GraphData.builder(this)
//                .setPointMap(pointMap)
//                .setGraphStroke(R.color.Black)
//                .setGraphGradient(R.color.BlueViolet, R.color.RoyalBlue)
//                .build();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                curveGraphView.setData(pointCount, Double.valueOf(indicator.getCriticalValue()).intValue(), gd);
//            }
//        }, 250);
    }
}
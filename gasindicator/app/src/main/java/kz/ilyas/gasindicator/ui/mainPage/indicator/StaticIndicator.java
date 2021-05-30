package kz.ilyas.gasindicator.ui.mainPage.indicator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Date;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.data.model.Statistics;
import kz.ilyas.gasindicator.ui.mainPage.MainPage;
import kz.ilyas.gasindicator.ui.mainPage.NotificationPush;


public class StaticIndicator extends AppCompatActivity {

    private TextView dateStart;
    private TextView id, address, position;
    private Button show;
    private Indicator indicator;
    private StaticIndicatorViewModel staticIndicatorViewModel;
    private ArrayList<Statistics> curStatics;
    private GraphView curveGraphView;
    private ListView dataView;
    private NotificationPush notificationPush;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_indicator);

        Bundle arguments = getIntent().getExtras();

        curveGraphView = findViewById(R.id.staticIndicator_graphView);
        dateStart = findViewById(R.id.staticIndicator_dateStart_value);
        show = findViewById(R.id.staticIndicator_show_button);
        id = findViewById(R.id.staticIndicator_id_value);
        address = findViewById(R.id.staticIndicator_address_value);
        position = findViewById(R.id.staticIndicator_position_value);
        dataView = findViewById(R.id.staticIndicator_recyclerDataView);
        notificationPush = new NotificationPush();

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
                    curStatics = staticIndicatorViewModel.sortTime(staticIndicatorViewModel.sort(statistics));
                    ArrayList<String> dates = new ArrayList<>();

                    Date cur = new Date();
                    for (Statistics curStatic : curStatics) {
                        int year = Integer.valueOf(curStatic.getDate().substring(0, 4));
                        int month = Integer.valueOf(curStatic.getDate().substring(5, 7));
                        int day = Integer.valueOf(curStatic.getDate().substring(8, 10));

                        Date arrayDate = new Date(year - 1900, month - 1, day,00,00,00);

                        if (cur.after(arrayDate)) {
                            dates.add(curStatic.getDate());
                        }
                    }
                    ArrayAdapter<String> indicatorAdapter = new ArrayAdapter<String>(StaticIndicator.this, android.R.layout.simple_list_item_1, dates);
                    dataView.setAdapter(indicatorAdapter);
                }
                findViewById(R.id.staticIndicator_progressBar).setVisibility(View.GONE);
                dateStart.setVisibility(View.VISIBLE);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateStart.getText().toString().equals("")) {
                    Toast.makeText(StaticIndicator.this, "Выберите дату", Toast.LENGTH_SHORT).show();
                } else {
                    if (curStatics.size() > 0) {
                        showGraph(curStatics, dateStart.getText().toString());
                    }
                }
            }
        });

        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataView.setVisibility(View.VISIBLE);
            }
        });

        dataView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dateStart.setText((String) parent.getItemAtPosition(position));
                dataView.setVisibility(View.GONE);
            }
        });

        staticIndicatorViewModel.getStatistics(indicator.getId());

    }

    private void showGraph(ArrayList<Statistics> newStat, String date) {
        curveGraphView.removeAllSeries();
        curveGraphView.getViewport().setScalable(true);
        curveGraphView.getViewport().setScrollable(true);
        curveGraphView.getViewport().setScalableY(true);
        curveGraphView.getViewport().setScrollableY(true);

        Statistics oneDay = new Statistics();

        int index = 0;
        for (int i = 0; i < newStat.size(); i++) {
            if (newStat.get(i).getDate().equals(date)) {
                oneDay = newStat.get(i);
                index = i;
                break;
            }
        }

        int lastDate = oneDay.getStatics().size() - 1;
        Date cur = new Date();

        int year = Integer.valueOf(oneDay.getDate().substring(0, 4));
        int month = Integer.valueOf(oneDay.getDate().substring(5, 7));
        int day = Integer.valueOf(oneDay.getDate().substring(8, 10));


        for (int i = 0; i < oneDay.getStatics().size(); i++) {
            int hh = Integer.valueOf(oneDay.getStatics().get(i).getTime().substring(0, 2));
            int mm = Integer.valueOf(oneDay.getStatics().get(i).getTime().substring(3, 5));
            int ss = Integer.valueOf(oneDay.getStatics().get(i).getTime().substring(6, 8));
            Date arrayDate = new Date(year - 1900, month - 1, day, hh, mm, ss);

            if (cur.before(arrayDate)) {
                lastDate = i;
                break;
            }
        }

        notificationPush.startPushBackgroud(StaticIndicator.this,oneDay,lastDate,indicator);

        DataPoint[] points = new DataPoint[lastDate];

        for (int i = 0; i < points.length; i++)
            points[i] = new DataPoint(i, oneDay.getStatics().get(i).getValueDouble());

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setColor(R.color.yellow);
        series.setDrawDataPoints(true);
        curveGraphView.addSeries(series);

        if (curveGraphView.getVisibility() != View.VISIBLE)
            curveGraphView.setVisibility(View.VISIBLE);

        int finalIndex = index;
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(StaticIndicator.this, "Время: " + curStatics.get(finalIndex).getStatics().get((int) dataPoint.getX()).getTime()
                                + "\nЗначение: " + dataPoint.getY()
                        , Toast.LENGTH_SHORT).show();

               // notificationPush.sendPush(StaticIndicator.this,"Push");
            }
        });

    }

//    public void sendPush(Context context, String body){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"ID_GAS_INDICATOR")
//                .setSmallIcon(R.drawable.ic_baseline)
//                .setContentTitle("Превышен уровень концетрации газа")
//                .setContentText(body)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "ID_GAS_INDICATOR";
//            String description = "ID_GAS_INDICATOR";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("ID_GAS_INDICATOR", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
////        Intent intent = new Intent(context, MainPage.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////
////        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
////
////        builder.setContentIntent(pendingIntent);
//
////        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//        notificationManager.notify((int) System.currentTimeMillis(),builder.build());
//    }
}
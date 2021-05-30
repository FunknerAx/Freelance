package kz.ilyas.gasindicator.ui.mainPage;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Indicator;
import kz.ilyas.gasindicator.data.model.Statistics;
import kz.ilyas.gasindicator.ui.mainPage.indicator.StaticIndicator;

public class NotificationPush  {
    private Context context;

    public void sendPush(Context context, String body){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"ID_GAS_INDICATOR")
                .setSmallIcon(R.drawable.ic_baseline)
                .setContentTitle("Превышен уровень концетрации газа")
                .setContentText(body);

//        Intent intent = new Intent(context, MainPage.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ID_GAS_INDICATOR";
            String description = "ID_GAS_INDICATOR";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ID_GAS_INDICATOR", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(),builder.build());

    }


    public void startPushBackgroud(StaticIndicator staticIndicator, Statistics oneDay, int lastDate, Indicator indicator) {

        AsyncTask.execute( new Runnable(){
            @Override
            public void run(){
                try {
                    for (int i = lastDate; i < oneDay.getStatics().size(); i++) {
                        {
                            if(oneDay.getStatics().get(i).getValueDouble() - 1 >= Double.valueOf(indicator.getCriticalValue())){
                                sendPush(staticIndicator, "Индикатор по адресу "+indicator.getAddress()+ " выявил критичное значение");
                            } else {
                                //sendPush(staticIndicator, "Все чисто");
                            }
                            Thread.sleep(120000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

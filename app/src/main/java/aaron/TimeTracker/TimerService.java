package aaron.TimeTracker;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    public TrackedTimer activeTimer = null;
    public Category activeCategory = null;
    Notification notification;
    NotificationCompat.Builder notificationBuilder;
    PendingIntent togglePendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        System.out.println("STARTED INTENT");

        String NOTIFICATION_CHANNEL_ID = "TimeTracker";
        String channelName = "Timer Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent toggleIntent = new Intent("TOGGLE_ACTIVE_TIMER");
        togglePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notification = notificationBuilder.setOngoing(true)
                .setContentTitle("No Timer Running")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Toggle", togglePendingIntent)
                .build();

        /*

                .setContentText("Timer Label")
                .setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .addAction(R.drawable.ic_launcher_foreground, "Toggle", togglePendingIntent)
         */



        startForeground(2, notification);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                activeTimer = TimersManager.timerFromId(intent.getStringExtra("timerId"));
                bindToTimer();
                activeTimer.running = true;
//                toggleIntent.putExtra("timer", activeTimer.getId());
            }
        };

        IntentFilter updateActiveTimerFilter = new IntentFilter("SET_ACTIVE_TIMER");
        registerReceiver(receiver, updateActiveTimerFilter);

        BroadcastReceiver updateTimersReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(activeTimer == null) return;
                double savedTimeElapsed = activeTimer.secondsElapsed + 0;
                boolean savedStartStopStatus = activeTimer.running;
                System.out.println("Updating timers at time " + savedTimeElapsed);
                activeTimer = TimersManager.timerFromId(activeTimer.getId());
                activeTimer.secondsElapsed = savedTimeElapsed;
                activeTimer.running = savedStartStopStatus;
                System.out.println("new time " + activeTimer.secondsElapsed);
            }
        };
        IntentFilter updateTimersFilter = new IntentFilter("UPDATE_TIMERS");
        registerReceiver(updateTimersReceiver, updateTimersFilter);


        registerReceiver(new BroadcastReceiver() {
            long lastToggle = new Date().getTime();
            @Override
            public void onReceive(Context context, Intent intent) {
                long diff = new Date().getTime() - lastToggle;
                lastToggle = new Date().getTime();

                if(diff < 250){
                    System.out.println("Toggled too quickly, ignoring");
                    return;
                }else{
                    System.out.println("Toggled after " + (diff) + "ms");
                }


                activeTimer.toggle();

                System.out.println("Heard toggle");
                NotificationManagerCompat.from(getApplicationContext()).notify(2,
                        notificationBuilder.setContentText(activeTimer.name + ": " + activeTimer.getTimeString())
                                .setSubText((activeTimer.running) ? "Running" : "Stopped")
                                .build()
                );


            }
        }, new IntentFilter("TOGGLE_ACTIVE_TIMER"));

        BroadcastReceiver selectCategoryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                activeCategory = CategoryManager.getCategoryFromId(intent.getStringExtra("categoryId"));
                System.out.println("Timerservice has received category: ");
                System.out.println(activeCategory);
            }
        };

        IntentFilter selectCategoryIntent = new IntentFilter("SELECT_ACTIVE_CATEGORY");

        registerReceiver(selectCategoryReceiver, selectCategoryIntent);



        BroadcastReceiver commitActiveTimeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(activeCategory != null){
                    activeCategory.commitTime(activeTimer.secondsElapsed);
                    Toast.makeText(getApplicationContext(), "Committed time to " + activeCategory.name, Toast.LENGTH_LONG).show();
                    activeTimer.secondsElapsed = 0;
                    activeTimer.pause();
                    Intent resyncTickIntent = new Intent("TIMER_TICK");
                    resyncTickIntent.putExtra("timerId", activeTimer.getId());
                    sendBroadcast(resyncTickIntent);
                }
            }
        };

        IntentFilter commitActiveTimeIntent = new IntentFilter("COMMIT_ACTIVE_TIMER_TIME");

        registerReceiver(commitActiveTimeReceiver, commitActiveTimeIntent);


        BroadcastReceiver syncingTickRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(activeTimer != null) {
                    Intent timerTick = new Intent("TIMER_TICK");
                    timerTick.putExtra("timerId", activeTimer.getId() + "");
                    getApplication().sendBroadcast(timerTick);
                }
            }
        };
        IntentFilter syncingTickRequestFilter = new IntentFilter("REQUEST_SYNCING_TICK");
        registerReceiver(syncingTickRequestReceiver, syncingTickRequestFilter);


        BroadcastReceiver syncingCategoryRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(activeCategory != null) {
                    Intent sync_active_category = new Intent("SYNC_ACTIVE_CATEGORY");
                    sync_active_category.putExtra("categoryId", activeCategory.getId() + "");
                    getApplication().sendBroadcast(sync_active_category);
                }
            }
        };
        IntentFilter syncingCategoryRequestReceiverFilter = new IntentFilter("REQUEST_ACTIVE_CATEGORY");
        registerReceiver(syncingCategoryRequestReceiver, syncingCategoryRequestReceiverFilter);

        bindToTimer();
        return super.onStartCommand(intent, flags, startId);
    }


    double lastSeconds = new Date().getTime() / 1000.0;
    double accumulatedTime = 0;
    Timer updateTimer = new Timer();
    boolean sendOneBroadcastAfterPause = true;
    public void bindToTimer(){
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                System.out.println("activeTimer is ");
//                System.out.println(activeTimer);
                if(activeTimer == null){
                    lastSeconds = new Date().getTime() / 1000.0;
                    return;
                }
                double nowSeconds = new Date().getTime() / 1000.0;
                double difference = (double) (nowSeconds - lastSeconds);
//                System.out.println("Diff: " + difference);
                lastSeconds = nowSeconds;
                activeTimer.tick(difference);
                if(activeTimer.running || sendOneBroadcastAfterPause) {
                    if(!activeTimer.running && sendOneBroadcastAfterPause){
                        sendOneBroadcastAfterPause = false;
                    }

                    if(activeTimer.running) sendOneBroadcastAfterPause = true;
                    Intent timerTick = new Intent("TIMER_TICK");
                    timerTick.putExtra("timerId", activeTimer.getId() + "");
                    getApplication().sendBroadcast(timerTick);

                    NotificationManagerCompat.from(getApplicationContext()).notify(2,
                            notificationBuilder.setContentText(activeTimer.name + ": " + activeTimer.getTimeString())
                                    .setSubText((activeTimer.running) ? "Running" : "Stopped")
                                    .setContentTitle("Running Timer")
                                    .build()
                    );
                }
            }
        }, 0, 500);
    }

    @Override
    public void onDestroy() {
//        System.out.println("DESTROYED INTENT");
        if(activeTimer != null)
            activeTimer.cancel();
        stopForeground(true);
        super.onDestroy();
    }
}

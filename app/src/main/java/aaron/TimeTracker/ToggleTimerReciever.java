package aaron.TimeTracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ToggleTimerReciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
//        System.out.println("Toggling " + TrackedTimer.timerFromId((String) intent.getExtras().get("timer")).name);
//        TrackedTimer.timerFromId((String) intent.getExtras().get("timer")).toggle();
    }
}

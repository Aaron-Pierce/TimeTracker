package aaron.TimeTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalTime;
import java.time.temporal.TemporalUnit;

public class EditTimerFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent requestSyncTickIntent = new Intent("REQUEST_SYNCING_TICK");
        getActivity().sendBroadcast(requestSyncTickIntent);

        IntentFilter timerTickFilter = new IntentFilter("TIMER_TICK");

        final DialogFragment root = this;
        final TrackedTimer[] active = {null};
        BroadcastReceiver tickReciever = new BroadcastReceiver() {

            boolean firstTime = true;
            @Override
            public void onReceive(Context context, Intent intent) {
                //This will run every time the timer ticks,
                //thus overwriting the user's set time every second.
                //this broadcast just gets the active timer, so we only
                //need it to run once anyway.
                if(!firstTime) return;
                firstTime = false;

                active[0] = TimersManager.timerFromId(intent.getStringExtra("timerId"));

                TimePicker startPicker = (TimePicker) view.findViewById(R.id.timerStart);
                LocalTime now = LocalTime.now().minusSeconds((long) active[0].secondsElapsed);
                startPicker.setHour(now.getHour());
                startPicker.setMinute(now.getMinute());
            }
        };

        getActivity().registerReceiver(tickReciever, timerTickFilter);

        view.findViewById(R.id.saveTimerEdits).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("saved");
                TimePicker timePickerStart = view.findViewById(R.id.timerStart);
                TimePicker timePickerEnd = view.findViewById(R.id.timerEnd);

                LocalTime endTime = LocalTime.of(timePickerEnd.getHour(), timePickerEnd.getMinute());
                LocalTime startTime = LocalTime.of(timePickerStart.getHour(), timePickerStart.getMinute());


                System.out.println("Endtime: ");
                System.out.println(endTime);
                System.out.println("Starttime: ");
                System.out.println(startTime);
                System.out.println("Diff: " + (endTime.compareTo(startTime)));

                if(endTime.compareTo(startTime) > 0){
                    LocalTime length = endTime.minusMinutes((startTime.getHour() * 60) + (startTime.getMinute()));
                    

                }

                root.dismiss();
            }
        });


    }
}

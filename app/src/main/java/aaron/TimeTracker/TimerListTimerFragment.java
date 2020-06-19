package aaron.TimeTracker;

import aaron.TimeTracker.R;
import android.content.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class TimerListTimerFragment extends Fragment {

    TrackedTimer timer;
    BroadcastReceiver tickReciever;

    public TimerListTimerFragment(TrackedTimer t) {
        this.timer = t;
    }

    public TimerListTimerFragment() {
        this.timer = new TrackedTimer("Blank", "Blank");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_list_timer, container, false);
        view.findViewById(R.id.timerListTimerFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Clicked on " + timer.name);

                MainActivity activity = (MainActivity) getActivity();
                activity.getViewPager().beginFakeDrag();
                activity.getViewPager().fakeDragBy(1000);
                activity.getViewPager().endFakeDrag();

                Intent setActiveTimerIntent = new Intent("SET_ACTIVE_TIMER");
                setActiveTimerIntent.putExtra("timerId", timer.getId());
                getActivity().sendBroadcast(setActiveTimerIntent);
            }
        });

        view.findViewById(R.id.timerListTimerFragment).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("Long clicked " + timer.name);
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete timer: " + timer.name)
                        .setMessage("Are you sure you want to delete this timer?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                System.out.println("Received click listener");
                                System.out.println(timer);
                                System.out.println(timer.running);
                                System.out.println("Clicked");
                                timer.delete();
                                Intent updateCategoriesIntent = new Intent("UPDATE_TIMERS");
                                getActivity().sendBroadcast(updateCategoriesIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView timerLabel = view.findViewById(R.id.timerTitle);
        TextView timerDescription = view.findViewById(R.id.timerDescription);
        final TextView timerTimeElapsed = view.findViewById(R.id.timerTimeElapsed);

        timerLabel.setText(this.timer.name);
        timerDescription.setText(this.timer.description);
        timerTimeElapsed.setText(this.timer.getTimeString());

        tickReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("timerId").equals(timer.getId())){
                    timerTimeElapsed.setText(timer.getTimeString());
                }
            }
        };

        IntentFilter tickFilter = new IntentFilter("TIMER_TICK");
        getActivity().registerReceiver(tickReciever, tickFilter);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(tickReciever);
        super.onDestroyView();
    }
}

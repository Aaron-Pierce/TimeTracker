package aaron.TimeTracker;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainFragment extends Fragment {
    MainPageAdapter adapter;
    ViewPager2 viewPager;
    public TrackedTimer activeTimer = null;
    BroadcastReceiver updateBroadcastReciever;
    BroadcastReceiver setActiveTimerReciever;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        runForegroundService();
        try {
            CategoryManager.loadCategories(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Intent requestSyncTickIntent = new Intent("REQUEST_SYNCING_TICK");
        getActivity().sendBroadcast(requestSyncTickIntent);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onDestroyView() {
        try {
            Objects.requireNonNull(getActivity()).unregisterReceiver(setActiveTimerReciever);
        } catch (IllegalArgumentException ignored) {
        }
        ;

        try {
            getActivity().unregisterReceiver(updateBroadcastReciever);
        } catch (IllegalArgumentException ignored) {
        }

        super.onDestroyView();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final View mainView = view;
        setActiveTimerReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TrackedTimer t = TimersManager.timerFromId(intent.getStringExtra("timerId"));
                Activity activity = getActivity();
                System.out.println(mainView.findViewById(R.id.activeTimerTimeElapsed));
                TextView activeTimerLabel = mainView.findViewById(R.id.activeTimerLabel);
                TextView activeTimerDescription = mainView.findViewById(R.id.activeTimerDescription);
                TextView activeTimerTimeElapsed = mainView.findViewById(R.id.activeTimerTimeElapsed);

                System.out.println("Setting active timer " + t);
                System.out.println(activeTimerLabel);
                activeTimerLabel.setText(t.name);
                activeTimerDescription.setText(t.description);
                activeTimerTimeElapsed.setText(t.getTimeString());
                activeTimer = t;
//                activeTimer.setView(mainView.findViewById(R.id.activeTimerLayout));
            }
        };

        IntentFilter setActiveTimerFilter = new IntentFilter("SET_ACTIVE_TIMER");
        Objects.requireNonNull(getActivity()).registerReceiver(setActiveTimerReciever, setActiveTimerFilter);


        updateBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                System.out.println("Received timer tick " + intent.getStringExtra("timerId"));
//                System.out.println("Activity running " + TrackedTimer.timerFromId(intent.getStringExtra("timerId")).running);
                TrackedTimer t = TimersManager.timerFromId(intent.getStringExtra("timerId"));
                if(activeTimer == null || !activeTimer.equals(t)){
                    activeTimer = t;
                    System.out.println("Changed active timer");
                }

//                System.out.println("Timer ticked, " + t.getTimeString() + "; " + t.name);
                TextView timeElapsed = ((TextView) mainView.findViewById(R.id.activeTimerTimeElapsed));
                timeElapsed.setText(t.getTimeString());

                TextView startStop = ((TextView) mainView.findViewById(R.id.toggleActiveTimerLink));
                startStop.setText(t.running ? "Stop" : "Start");

                TextView label = ((TextView) mainView.findViewById(R.id.activeTimerLabel));
                label.setText(t.name);
                TextView description = ((TextView) mainView.findViewById(R.id.activeTimerDescription));
                description.setText(t.description);

            }
        };

        IntentFilter filter = new IntentFilter("TIMER_TICK");
        getActivity().registerReceiver(updateBroadcastReciever, filter);


        TextView toggleActiveTimerLink = (TextView) view.findViewById(R.id.toggleActiveTimerLink);
        toggleActiveTimerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeTimer != null) {
                    ((TextView) v).setText(activeTimer.running ? "START" : "STOP");
                    Intent toggleActiveTimerIntent = new Intent("TOGGLE_ACTIVE_TIMER");
                    getActivity().sendBroadcast(toggleActiveTimerIntent);
                }else{

                }
            }
        });

        view.findViewById(R.id.editActiveTimerLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeTimer == null) return;
                System.out.println("Edit running timer");
                EditTimerFragment editTimerFragment = new EditTimerFragment();
                editTimerFragment.show(getChildFragmentManager(), "edit_timer_dialog_fragment");
            }
        });

        TextView mainHeader = view.findViewById(R.id.mainHeader);
        final MainFragment savedThis = this;
        mainHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeveloperSheetFragment sheetFragment = new DeveloperSheetFragment(savedThis);
                sheetFragment.show(getChildFragmentManager(), "dev_fragment_manager");
                return true;
            }
        });

        CategoryManager.printTimes();

        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentCategoryList, new CategoryListFragment(), "category_list_fragment")
                .commit();

    }

    public boolean foregroundServiceRuning = false;

    public void runForegroundService() {
        Intent intent = new Intent(getActivity(), TimerService.class);
        ComponentName serviceName = getActivity().startForegroundService(intent);
        foregroundServiceRuning = true;
    }

    public void stopForegroundService() {
        Intent intent = new Intent(getActivity(), TimerService.class);
        getActivity().stopService(intent);
        foregroundServiceRuning = false;
    }



    @Override
    public void onDestroy() {
        try {
            CategoryManager.writeOut(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            TimersManager.writeOutTimers(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}

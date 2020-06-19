package aaron.TimeTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TimersFragment extends Fragment {

    MainPageAdapter adapter;
    ViewPager2 viewPager;
    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timers, container, false);
    }


    BroadcastReceiver updateReciever;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(updateReciever);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        fragmentView = view;
//        for(int i = 0; i < 20; i++) {
//            timers.add(new TrackedTimer("Ex. Timer " + i, "Desc."));
//        }

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("Create new ting");
                AddTimerFragment addTimerDialog = new AddTimerFragment();
                addTimerDialog.show(getChildFragmentManager(), "add_timer_dialog_fragment");
            }
        });


        Objects.requireNonNull(getActivity()).registerReceiver(updateReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTimers();
            }
        }, new IntentFilter("UPDATE_TIMERS"));


        try {
            TimersManager.loadTimers(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent updateTimersIntent = new Intent("UPDATE_TIMERS");
        getActivity().sendBroadcast(updateTimersIntent);
    }

    public void updateTimers() {
        System.out.println("Updating timers");

        LinearLayout timersListView = getView().findViewById(R.id.timersListView);
        timersListView.removeAllViewsInLayout();

        for (TrackedTimer t : TimersManager.timers) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            TimerListTimerFragment fragment = new TimerListTimerFragment(t);
            fragmentTransaction.add(R.id.timersListView, fragment);
            fragmentTransaction.commit();
        }
    }


}

package aaron.TimeTracker;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Aaron on 6/19/2020
 */
public class EditTimerCommitTimePopup extends DialogFragment{

    TrackedTimer toCommit = null;
    Date date = null;
    DialogFragment parentDialog;
    public EditTimerCommitTimePopup(TrackedTimer timer, Date dateToCommitAt, DialogFragment parentDialog){
        toCommit = timer;
        date = dateToCommitAt;
        this.parentDialog = parentDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Created edittimerpopupo******************************");
        System.out.println("Container");
        System.out.println(container);
        return inflater.inflate(R.layout.fragment_edit_timer_commit_time, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println("Created edittimercommittimepopup*****************");
        final TrackedTimer timer = toCommit;
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentCategoryListCommitTimePopup, new CategoryListFragment(), "category_list_fragment")
                .runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        view.findViewById(R.id.fragmentCategoryListCommitTimePopup).findViewById(R.id.seeAllTimersLink).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Category activeCategory = CategoryManager.tryToFindActiveCategory();
                                if(activeCategory != null){
                                        sendCommitTime(activeCategory, timer);
                                }else {
                                    BroadcastReceiver requestActiveCategoryReceiver = new BroadcastReceiver() {
                                        @Override
                                        public void onReceive(Context context, Intent intent) {
                                            System.out.println("Received requestActgiveCategoryResponse");
                                            Category activeCategory = CategoryManager.getCategoryFromId(intent.getStringExtra("categoryId"));
                                                if(activeCategory != null){
                                                    sendCommitTime(activeCategory, timer);
                                                }
                                            getActivity().unregisterReceiver(this);
                                        }
                                    };
                                    IntentFilter requestActiveCategoryFilter = new IntentFilter("SYNC_ACTIVE_CATEGORY");
                                    getActivity().registerReceiver(requestActiveCategoryReceiver, requestActiveCategoryFilter);

                                    getActivity().sendBroadcast(new Intent("REQUEST_ACTIVE_CATEGORY"));
                                }
                            }

                            private void sendCommitTime(Category activeCategory, TrackedTimer timer){
                                activeCategory.commitTime(new CommittedTime(activeCategory.getId(), timer.secondsElapsed, date));
                                Toast.makeText(getContext(), "Committed time to " + activeCategory.name, Toast.LENGTH_LONG).show();

                                System.out.println("Committed time from edit )()()()()()()()()())");

                                timer.secondsElapsed = 0;
                                timer.pause();
                                Intent resyncTickIntent = new Intent("TIMER_TICK");
                                resyncTickIntent.putExtra("timerId", timer.getId());
                                Objects.requireNonNull(getActivity()).sendBroadcast(resyncTickIntent);
                                dismiss();
                                parentDialog.dismiss();
                            }
                        });
                    }
                })
                .commit();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        System.out.println("Destroyed edittimecommmittimepopup***********");
        super.onDestroyView();
    }
}

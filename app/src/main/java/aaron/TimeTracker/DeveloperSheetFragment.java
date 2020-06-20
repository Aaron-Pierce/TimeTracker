package aaron.TimeTracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.ZoneId;

public class DeveloperSheetFragment extends BottomSheetDialogFragment {
    MainFragment mainFragment;
    public DeveloperSheetFragment(MainFragment fragment){
        mainFragment = fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer_sheet, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView setTimerTime = view.findViewById(R.id.setTimerTimer);
        setTimerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.activeTimer.secondsElapsed = 120 * 60 + 1;
            }
        });

        Button clearCommitedTimesButton = view.findViewById(R.id.clearCommitedTimesButton);
        clearCommitedTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Category c : CategoryManager.categories){
                    CommitedTimeCollection toBuild = new CommitedTimeCollection();
                    for(CommittedTime t : c.committedTimes){
                        LocalDate d = LocalDate.from(t.date.toInstant().atZone(ZoneId.systemDefault()));
                        LocalDate now = LocalDate.now();
                        if(!d.isEqual(now)){
                            toBuild.add(t);
                        }
                    }

                    c.committedTimes = toBuild;
                }
            }
        });
    }
}

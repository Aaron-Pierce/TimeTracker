package aaron.TimeTracker;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

public class DayReportLabelSpacerFragment extends Fragment {
    CommittedTime associatedTime;
    CommittedTime lastTime;
    public DayReportLabelSpacerFragment(CommittedTime c, CommittedTime last) {
        associatedTime = c;
        lastTime = last;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_report_label_spacer, container, false);
    }


    public int pixelsToDp(int pixels){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout spacer = view.findViewById(R.id.dayReportSpacer);
        ViewGroup.LayoutParams params = spacer.getLayoutParams();

        ZonedDateTime nowZoned = ZonedDateTime.now();

        Instant endOfLastBarInstant = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).plusHours(5).toInstant();
        if(lastTime != null) {
            endOfLastBarInstant = lastTime.date.toInstant();
        }

        Duration duration = Duration.between(endOfLastBarInstant, associatedTime.date.toInstant());
        long minutes = duration.getSeconds() / 60;
        params.height = pixelsToDp((int) minutes / 2);
        params.height -= pixelsToDp((int) (associatedTime.seconds / 60 / 2));
        params.height -= pixelsToDp(15); //offset text size
        System.out.println("space after " + minutes + " minutes, or " + (minutes / 60) + " hours past last time");
        params.width = pixelsToDp(10);
        spacer.setLayoutParams(params);
    }
}

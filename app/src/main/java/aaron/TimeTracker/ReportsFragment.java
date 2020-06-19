package aaron.TimeTracker;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

public class ReportsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    public boolean datesOnSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public int pixelsToDp(int pixels){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout dayReportBar = view.findViewById(R.id.dayReportBarLayout);

        ViewGroup.LayoutParams params = dayReportBar.getLayoutParams();
        int height = (22 - 5) * 60 / 2;
//        if(height < 20) height = 20;
        params.height = pixelsToDp(height);
        params.width = pixelsToDp(10);
        dayReportBar.setLayoutParams(params);

        ArrayList<CommittedTime> timeInLastDay = new ArrayList<>();
        Date now = new Date();
        for(Category c : CategoryManager.categories){
            for(int i = c.committedTimes.size() - 1; i >= 0; i--){
                CommittedTime checkTime = c.committedTimes.get(i);
                if(datesOnSameDay(now, checkTime.date)){
                    timeInLastDay.add(checkTime);
                }
            }
        }


        System.out.println("Times today: ");
        Collections.sort(timeInLastDay);
        for(CommittedTime c : timeInLastDay){
            ZonedDateTime nowZoned = ZonedDateTime.now();
            Instant midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).toInstant();
            Instant fiveAm = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).plusHours(5).toInstant();
            Duration duration = Duration.between(midnight, c.date.toInstant());
            Duration durationSinceBeginningOfTimeline = Duration.between(fiveAm, c.date.toInstant());
            long seconds = duration.getSeconds();
            System.out.println(c.seconds + " on " + c.date);
            System.out.println("Which is " + durationSinceBeginningOfTimeline.getSeconds() + " seconds into the bar, or " + (100 * (durationSinceBeginningOfTimeline.getSeconds() / ((17 * 60 * 60.0)))) + "% through the bar");
        }


        LinearLayout bar = view.findViewById(R.id.dayReportBarLayout);
        bar.removeAllViewsInLayout();
        CommittedTime last = null;
        for(CommittedTime c : timeInLastDay){
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.dayReportBarLayout, new DayReportSpacerFragment(c, last));
            fragmentTransaction.add(R.id.dayReportBarLayout, new DayReportBarFragment(c, CategoryManager.getCategoryFromId(c.parentCategoryId)));
            fragmentTransaction.commit();
            last = c;
        }


        RelativeLayout labels = view.findViewById(R.id.todayTimelineLabels);
        labels.removeAllViewsInLayout();
        for(CommittedTime c : timeInLastDay){
            if(c.seconds > 30 * 60) { //30 minutes or 15 pixels, the label height
                TextView label = new TextView(getContext());
                ZonedDateTime nowZoned = ZonedDateTime.now();
                Instant beginningOfDayInstant = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).plusHours(5).toInstant();

                Duration duration = Duration.between(beginningOfDayInstant, c.date.toInstant());
                long minutes = duration.getSeconds() / 60;
                int yVal = pixelsToDp((int) minutes / 2);
                yVal -= pixelsToDp((int) (c.seconds / 60 / 2));
                yVal -= 15;

                System.out.println("minutes since beginning of day " + minutes + " which is " + minutes/60 + "hours");

                label.setText(Objects.requireNonNull(CategoryManager.getCategoryFromId(c.parentCategoryId)).name);
                System.out.println("Placing at yval " + yVal);
                RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                relParams.topMargin = yVal;
                label.setLayoutParams(relParams);
                labels.addView(label);
            }


        }




    }


}

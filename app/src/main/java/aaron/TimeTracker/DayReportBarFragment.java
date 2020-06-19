package aaron.TimeTracker;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DayReportBarFragment extends Fragment {
    CommittedTime time;
    Category associatedCategory;

    public DayReportBarFragment(CommittedTime c, Category associatedCategory) {
        this.time = c;
        this.associatedCategory = associatedCategory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_report_bar, container, false);
    }


    public int pixelsToDp(int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View bar = view.findViewById(R.id.reportBar);

        ViewGroup.LayoutParams params = bar.getLayoutParams();
        int height = (int) (time.seconds / 60) / 2; //2 minutes to a pixel
//        if(height < 20) height = 20;
        params.height = pixelsToDp(height);
        params.width = pixelsToDp(10);
        bar.setLayoutParams(params);
        System.out.println(view);
        System.out.println("Color " + associatedCategory.color);

        int barColor = associatedCategory.color;
        if (associatedCategory.color == null) {
            StringBuilder color = new StringBuilder("#");
            for (int i = 0; i < 3; i++)
                color.append(Integer.toHexString((int) (Math.random() * 200 + 55)));
            barColor = Color.parseColor(String.valueOf(color));
        }
        bar.setBackgroundColor(barColor);
        System.out.println("added bar " + Math.floor(time.seconds / 60) + "minutes long at " + time.date);

        Rect offsetViewBounds = new Rect();
//returns the visible bounds
        view.getDrawingRect(offsetViewBounds);
// calculates the relative coordinates to the parent
        ((ViewGroup)view.getParent()).offsetDescendantRectToMyCoords(view, offsetViewBounds);

        int relativeTop = offsetViewBounds.top;
        int relativeLeft = offsetViewBounds.left;
        System.out.println(relativeTop + " is reltop");
    }
}

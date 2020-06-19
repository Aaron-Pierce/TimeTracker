package aaron.TimeTracker;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DayReportBarLabelFragment extends Fragment {
    CommittedTime time;
    Category associatedCategory;
    public DayReportBarLabelFragment(CommittedTime c, Category associatedCategory) {
        this.time = c;
        this.associatedCategory = associatedCategory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_report_bar_label, container, false);
    }


    public int pixelsToDp(int pixels){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView label = view.findViewById(R.id.dayReportBarLabelText);
        label.setText(associatedCategory.name);
    }
}

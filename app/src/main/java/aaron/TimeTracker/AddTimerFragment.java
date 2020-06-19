package aaron.TimeTracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class AddTimerFragment extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_timer_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View sheet = view;
        view.findViewById(R.id.addNewTimerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(sheet.findViewById(R.id.timerLabelInput));
                String label = ((EditText)sheet.findViewById(R.id.timerLabelInput)).getText().toString();
                String description = ((EditText)sheet.findViewById(R.id.timerDescriptionInput)).getText().toString();
//                System.out.println("New timer label: " + label +  " description: " + description);
                boolean successfullyAdded = TimersManager.addTimer(new TrackedTimer(label, description));
                if(!successfullyAdded){
                    Toast.makeText(getContext(), "Timer with same name already exists", Toast.LENGTH_SHORT).show();
                    System.out.println(Arrays.deepToString(TimersManager.timers.toArray()));
                }else {
                    Intent updateTimersIntent = new Intent();
                    updateTimersIntent.setAction("UPDATE_TIMERS");
                    Objects.requireNonNull(getActivity()).sendBroadcast(updateTimersIntent);
                }
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}

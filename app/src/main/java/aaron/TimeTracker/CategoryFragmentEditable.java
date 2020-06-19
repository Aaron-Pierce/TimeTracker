package aaron.TimeTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryFragmentEditable extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_editable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View newCategoryView = view;
        view.findViewById(R.id.confirmAddedCategoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("emitting");
                String name = ((TextView) newCategoryView.findViewById(R.id.categoryLabelInput)).getText().toString();
                String description = ((TextView)newCategoryView.findViewById(R.id.categoryDescriptionInput)).getText().toString();
                Category newCategory = new Category(name, description);
                CategoryManager.addCategory(newCategory);
                Intent updateCategoriesIntent = new Intent("UPDATE_CATEGORIES");
                getActivity().sendBroadcast(updateCategoriesIntent);
                System.out.println("Made it to the end?");
                dismiss();
            }
        });
    }
}

package aaron.TimeTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.impl.constraints.trackers.BroadcastReceiverConstraintTracker;

public class CategoryListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    BroadcastReceiver updateCategoriesReceiver;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println("Created view!!!!");
        view.findViewById(R.id.addNewCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add new category");
                CategoryFragmentEditable addCategoryDialog = new CategoryFragmentEditable();
                addCategoryDialog.show(getChildFragmentManager(), "add_category_dialog_fragment");
            }
        });

        updateCategoriesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateCategories();
            }
        };
        IntentFilter updateCategoriesFilter = new IntentFilter("UPDATE_CATEGORIES");
        getActivity().registerReceiver(updateCategoriesReceiver, updateCategoriesFilter);

        System.out.println("About to call update categories");
        updateCategories();
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(updateCategoriesReceiver);
        super.onDestroyView();
    }

    public void updateCategories() {
        System.out.println("Updating Categories");
        System.out.println("85734098573240958732490587324095873248905");
        LinearLayoutCompat categoriesListView = getView().findViewById(R.id.categoriesContainer);
        categoriesListView.removeAllViewsInLayout();
        for (CategoryFragment fragment : CategoryManager.generateCategoryFragments()) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.categoriesContainer, fragment);
            fragmentTransaction.commit();
        }
    }
}

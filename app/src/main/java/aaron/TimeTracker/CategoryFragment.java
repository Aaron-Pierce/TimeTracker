package aaron.TimeTracker;


import android.content.*;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import org.w3c.dom.Text;

public class CategoryFragment extends Fragment {
    Category category;
    public CategoryFragment(Category category) {
        this.category = category;
    }

    public CategoryFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    boolean justActivated = false;
    BroadcastReceiver highlightReceiver;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View categoryView = view;
        highlightReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Selected");
                TextView categorySelectedIndicator = view.findViewById(R.id.categorySelectedIndicator);
                categorySelectedIndicator.setText((justActivated) ? "SELECTED" : "");
                category.selected = justActivated;
                justActivated = false;
            }
        };

        try {
            ((TextView) view.findViewById(R.id.categoryLabel)).setText(category.name);
            ((TextView) view.findViewById(R.id.categoryDescription)).setText(category.description);
        }catch (NullPointerException ignored){}
        IntentFilter highlightIntentFilter = new IntentFilter("SELECT_ACTIVE_CATEGORY");
        getActivity().registerReceiver(highlightReceiver, highlightIntentFilter);


        System.out.println(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked");
                justActivated = true;
                Intent selectThisIntent = new Intent("SELECT_ACTIVE_CATEGORY");
                selectThisIntent.putExtra("categoryId", category.getId());
                getActivity().sendBroadcast(selectThisIntent);

            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("Long clicked " + category.name);
                if(category.selected){
                    Toast.makeText(getContext(), "You cannot delete a selected category", Toast.LENGTH_SHORT).show();
                    return true;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete category: " + category.name)
                        .setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!category.selected) {
                                    category.delete();
                                    Intent updateCategoriesIntent = new Intent("UPDATE_CATEGORIES");
                                    getActivity().sendBroadcast(updateCategoriesIntent);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            getActivity().unregisterReceiver(highlightReceiver);
        }catch (IllegalArgumentException ignored){};
    }
}

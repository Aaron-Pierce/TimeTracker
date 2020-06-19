package aaron.TimeTracker;

import android.animation.Animator;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPageAdapter extends FragmentStateAdapter {
    public MainPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
//        System.out.println(position);
        Fragment fragment;
        if(position == 0){
            fragment = new MainFragment();
        }else if(position == 1){
            fragment = new TimersFragment();
        }else{
            fragment = new ReportsFragment();
        }
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt("object", position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

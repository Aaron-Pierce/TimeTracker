package aaron.TimeTracker;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private static final Object ONGOING_NOTIFICATION_ID = 2;
//    private static final String CHANNEL_DEFAULT_IMPORTANCE = 1;


    ViewPager2 mainViewPager;
    TrackedTimer activeTimer;

    public ViewPager2 getViewPager() {
        return mainViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView seeAllLink = findViewById(R.id.seeAllTimersLink);
//        seeAllLink.setPaintFlags(seeAllLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mainViewPager = findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(new MainPageAdapter(this));
        mainViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        mainViewPager.setAdapter(new MainPageAdapter());
    }

    public void commitTimeButtonClicked(View view) {
        Intent commitTimeIntent = new Intent("COMMIT_ACTIVE_TIMER_TIME");
        sendBroadcast(commitTimeIntent);
    }

    public void quickStart1(View view) {
        Toast.makeText(view.getContext(), "Clicked Quickstart 1", 0).show();
    }

    public void quickStart2(View view) {
        Toast.makeText(view.getContext(), "Clicked Quickstart 2", 0).show();
    }

    public void quickStart3(View view) {
        Toast.makeText(view.getContext(), "Clicked Quickstart 3", 0).show();
    }
}
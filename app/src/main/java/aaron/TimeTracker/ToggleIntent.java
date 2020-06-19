package aaron.TimeTracker;

import android.content.Context;
import android.content.Intent;

import java.util.Timer;

public class ToggleIntent extends Intent {
    Timer t;
    public ToggleIntent(Context packageContext, Class<?> cls, Timer timer) {
        super(packageContext, cls);
        t = timer;
    }

    public Timer getTimer(){
        return t;
    }
}

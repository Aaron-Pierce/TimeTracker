package aaron.TimeTracker;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import java.io.*;
import java.util.*;

public class TrackedTimer extends Timer implements Serializable {
    String id;
    Boolean running = false;
    double secondsElapsed = 0;

    String name;
    String description;

    transient View primaryView;

    transient long lastToggle = new Date().getTime();

    public TrackedTimer(){

    }

    public TrackedTimer(String name, String shortDescription){
        this.name = name;
        this.description = shortDescription;
        this.id = name.hashCode() + "";
    }

    @Override
    public void cancel() {
        running = false;
        super.cancel();
    }

    public String getId() {
        return id;
    }

    public void toggle() {
        System.out.println("Running toggle()");
        long diff = new Date().getTime() - lastToggle;
        lastToggle = new Date().getTime();
        if(Math.abs(diff) < 200){
            System.out.println("Skipped toggle from toggle()");
            return;
        }
        if(running){
            this.pause();
        }else{
            this.resume();
        }
    }

    public void resume() {
        this.running = true;
    }

    public void pause() {
        this.running = false;
    }

    public String getTimeString(){
        int hours = (int) Math.floor(secondsElapsed / 3600);
        String hoursString = Math.floor(hours / 10.0) > 0 ? hours + "" : "0" + hours;
        int minutes = (int) Math.floor((secondsElapsed - (3600 * hours)) / 60);
        String minutesString = Math.floor(minutes / 10.0) > 0 ? minutes + "" : "0" + minutes;
        int seconds = (int)secondsElapsed - (hours * 3600) - (minutes * 60);
        String secondsString = Math.floor(seconds / 10.0) > 0 ? seconds + "" : "0" + seconds;
        String timeString = hoursString + ":" + minutesString + ":" + secondsString;
        return timeString;
    }

    public void tick(){
//            System.out.println("Tick before running");
            if(running) {
//                System.out.println("Tick and running");
                secondsElapsed++;
            }
    }

    public void tick(double passed){
        if(running){
            secondsElapsed += passed;
        }else{
//            System.out.println("Tick but not running");
        }
    }

    public void setView(View activeTimerLayout){
        this.primaryView = activeTimerLayout;
//        System.out.println("Set trackedtimer view to: ");
//        System.out.println(this.primaryView);
    }

    public void delete(){
        TimersManager.delete(this);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof TrackedTimer){
            return ((TrackedTimer)obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}

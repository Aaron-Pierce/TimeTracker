package aaron.TimeTracker;

import android.view.inspector.PropertyReader;

import java.io.Serializable;
import java.util.Date;

public class CommittedTime implements Serializable, Comparable {
    double seconds;
    Date date;
    String parentCategoryId;

    public CommittedTime(){

    }

    public CommittedTime(String parentCategoryId, double seconds){
        this.seconds = seconds;
        this.date = new Date();
        this.parentCategoryId = parentCategoryId;
    }

    public CommittedTime(String parentCategoryId, double seconds, Date date){
        this.seconds = seconds;
        this.date = date;
        this.parentCategoryId = parentCategoryId;
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof CommittedTime){
            return this.date.compareTo(((CommittedTime) o).date);
        }else{
            throw new IllegalArgumentException();
        }
    }
}

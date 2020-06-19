package aaron.TimeTracker;

import android.graphics.Color;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class Category implements Serializable {
    transient public boolean selected = false;
    String name;
    String description;
    Integer color;
    CommitedTimeCollection committedTimes = new CommitedTimeCollection();
    private static final long serialVersionUID = 5L;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.color = generateRandomColor();
    }

    private int generateRandomColor(){
        StringBuilder colorString = new StringBuilder("#");
        for(int i = 0; i < 3; i++){
            colorString.append(Integer.toHexString((int) (Math.random() * 240 + 15) ));
        }
        return Color.parseColor(String.valueOf(colorString));
    }

    public boolean equals(Category obj) {
        if(obj.name == null) return false;
        return obj.name.equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void delete() {
        CategoryManager.delete(this);
    }

    public String getId() {
        return this.name.hashCode()+"";
    }

    public void commitTime(double time){
        if(time == 0) return;
        System.out.println("Committing time with id " + getId());
        committedTimes.add(new CommittedTime(getId(), time));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Category){
            return ((Category) obj).getId().equals(getId());
        }
        assert obj != null;
        return this.hashCode() == obj.hashCode();
    }
}

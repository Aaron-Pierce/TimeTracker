package aaron.TimeTracker;

import android.content.Context;
import android.webkit.TracingConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TimersManager {
    static ArrayList<TrackedTimer> timers = new ArrayList<>();
    public static HashMap<String, TrackedTimer> idMap = new HashMap<>();


    public static TrackedTimer timerFromId(String id){
        return idMap.get(id);
    }

    public static boolean addTimer(TrackedTimer t){
        if(timers.contains(t)) return false;
        else {
            timers.add(t);
            idMap.put(t.getId(), t);
            return true;
        }
    }


    public static void repopulateIdMap(){
        idMap = new HashMap<>();
        for(TrackedTimer t : timers){
            idMap.put(t.getId(), t);
        }
    }


    public static void writeOutTimers(Context ctx) throws IOException {
        File file = new File(ctx.getFilesDir(), "timers");
        file.createNewFile();
        FileOutputStream writer = new FileOutputStream(file, false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(writer);
        objectOutputStream.writeObject(timers);

        objectOutputStream.close();
        writer.close();
    }

    public static void loadTimers(Context ctx) throws IOException, ClassNotFoundException {
        File file = new File(ctx.getFilesDir(), "timers");
        FileInputStream reader = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(reader);
        Object read = objectInputStream.readObject();
        if(read instanceof ArrayList){
            timers = (ArrayList<TrackedTimer>) read;
        }

        objectInputStream.close();
        reader.close();

        repopulateIdMap();
        System.out.println("ID MAP SIZE: " + idMap.size());
    }

    public static void delete(TrackedTimer trackedTimer) {
        System.out.println("List size before " + timers.size());
        timers.remove(trackedTimer);
        System.out.println("List size after " + timers.size());
        repopulateIdMap();
    }
}

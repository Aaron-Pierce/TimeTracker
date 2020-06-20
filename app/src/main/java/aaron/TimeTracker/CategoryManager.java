package aaron.TimeTracker;

import android.content.Context;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoryManager {
    static ArrayList<Category> categories = new ArrayList<>();
    static HashMap<String, Category> categoriesIdMap = new HashMap<>();

    public static void addCategory(Category c){
        if(!(categories.contains(c))){
            System.out.println("Map doesn't contain category");
            categories.add(c);
            categoriesIdMap.put(c.getId(), c);
        }else{
            System.out.println("Category already contains: " + categories.contains(c));
        }
    }

    public static void repopulateCategoryIdMap(){
        categoriesIdMap = new HashMap<>();
        for(Category c : categories){
            categoriesIdMap.put(c.getId(), c);
        }
    }

    public static Category getCategoryFromId(String id){
        for(Category c : categories){
            if(c.getId().equals(id)) return c;
        }
        return null;
    }

    public static CategoryFragment[] generateCategoryFragments(){
        CategoryFragment[] fragments = new CategoryFragment[categories.size()];
        for(int i = 0; i < categories.size(); i++){
            fragments[i] = new CategoryFragment(categories.get(i));
        }
        return fragments;
    }

    public static void writeOut(Context ctx) throws IOException {
        File file = new File(ctx.getFilesDir(), "categories");
        file.createNewFile();
        FileOutputStream writer = new FileOutputStream(file, false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(writer);
        objectOutputStream.writeObject(categories);

        objectOutputStream.close();
        writer.close();

    }

    public static void loadCategories(Context ctx) throws IOException, ClassNotFoundException {
        File file = new File(ctx.getFilesDir(), "categories");
        FileInputStream writer = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(writer);
        Object read = objectInputStream.readObject();
        if(read instanceof ArrayList){
            categories = (ArrayList<Category>) read;
        }

        repopulateCategoryIdMap();

        objectInputStream.close();
        writer.close();


        for(Category c : categories){
            for(CommittedTime t : c.committedTimes){
                System.out.println("Id of category " + t.parentCategoryId);
                System.out.println("And category id is actually " + c.getId());
//                if(t.seconds > 15 * 60) t.seconds = 30 * 60 + 1;
//                t.seconds = 60 * 42;
//                c.delete();
            }
        }
    }

    public static void delete(Category category) {
        categories.remove(category);
    }

    public static void printTimes() {
        for(Category c : categories){
            for(CommittedTime time : c.committedTimes){
                System.out.println(c.name + ": " + "On " + time.date + "; " + time.seconds);
            }
        }
    }

    public static Category tryToFindActiveCategory() {
        for(Category c : categories){
            if(c.selected) return c;
        }
        return null;
    }
}

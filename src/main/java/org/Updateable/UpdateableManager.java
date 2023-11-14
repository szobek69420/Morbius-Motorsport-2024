package main.java.org.Updateable;

import java.util.ArrayList;

public class UpdateableManager {
    private ArrayList<Updateable> updateables;

    public UpdateableManager(){
        updateables=new ArrayList<>();
    }

    public void update(double deltaTime){
        for (Updateable u: updateables){
            u.update(deltaTime);
        }
    }

    public void addUpdateable(Updateable u){
        updateables.add(u);
    }

    public void removeUpdateable(Updateable u){
        updateables.remove(u);
    }
}

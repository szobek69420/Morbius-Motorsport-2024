package main.java.org.Physics;

import java.util.ArrayList;

public class CollisionDetection {

    private ArrayList<AABB> nonKinematic;
    private ArrayList<AABB> kinematic;

    public CollisionDetection(){
        nonKinematic=new ArrayList<>();
        kinematic=new ArrayList<>();
    }

    public void addAABB(AABB aabb){
        if(aabb.isKinematic)
            kinematic.add(aabb);
        else
            nonKinematic.add(aabb);
    }

    public void CalculatePhysics(double deltaTime){
        for(AABB aabb : nonKinematic)
            aabb.move(deltaTime);

        for(AABB aabb:nonKinematic){
            for (AABB aabb2:kinematic){
                AABB.resolveCollision(aabb,aabb2);
            }
        }
    }
}

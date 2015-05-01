package csc591.bucketlistraleigh.helper;

import android.os.Build;

/**
 * Created by Tamil on 4/30/2015.
 */
public class Building {

    private static Building instance = null;
    private String buildingId;
    private String buildingName;
    protected Building(){

    }

    public static Building getInstance(){
        if(instance == null){
            instance = new Building();
        }
        return instance;
    }


    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}

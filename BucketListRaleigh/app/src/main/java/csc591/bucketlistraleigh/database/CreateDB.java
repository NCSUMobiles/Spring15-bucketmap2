package csc591.bucketlistraleigh.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by Nupur on 4/8/2015.
 */
public class CreateDB extends SQLiteOpenHelper{


    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "crud.db";
    SQLiteDatabase db;


    public CreateDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_Building_Coordinate = "CREATE TABLE IF NOT EXISTS " +"Building_Coordinates(" +
                "building_id INTEGER," +
                "building_name VARCHAR," +
                "x_coordinate REAL," +
                "y_coordinate REAL)";
        db.execSQL(CREATE_TABLE_Building_Coordinate);
        putBuildingData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS Building_Coordinates");
        // Create tables again
        onCreate(db);

    }

    public void createDatabase(){

    }

    public void putBuildingData(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Building_Coordinates(building_id INTEGER,building_name VARCHAR,x_coordinate REAL,y_coordinate REAL);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(1,'Flying Saucer',7.840, 4.550);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(2,'Lincoln Theatre',8.030, 8.420);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(3,'Cafe de Los Muertos',7.450,5.050);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(4,'Poole’s Diner',6.150, 6.930);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(5,'Raleigh Times',9.770, 6.770);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(6,'Askew-Taylor Point',8.000, 3.0030);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(7,'Beasley Chicken and Honey',9.500, 7.140);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(8,'Bida Manda',10.180, 7.330);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(9,'Oakwood Cafe',13.240, 6.830);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(10,'Lincoln Theatre',8.030 , 8.420);");
        Cursor building_name = db.rawQuery("SELECT * from Building_Coordinates", null);
        if (building_name.moveToFirst()){
            do{
                String data = building_name.getString(building_name.getColumnIndex("building_name"));
                Log.i("From seperate DB class", data + " ");
                // do what ever you want here
            }while(building_name.moveToNext());
        }
        building_name.close();

    }
}

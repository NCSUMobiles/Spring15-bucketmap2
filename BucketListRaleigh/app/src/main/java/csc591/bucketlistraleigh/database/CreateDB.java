package csc591.bucketlistraleigh.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nithya Pari on 4/19/2015
 * CreateDB.java creates the database and all the tables needed for the Bucket Map Raleigh Project
 */
public class CreateDB extends SQLiteOpenHelper{

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "bucketlistraleigh.db";


    /* Function Name: CreateDB
    *
    */
    public CreateDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /* Function Name: onCreate
    * This function creates the following tables
    * -> login
    * -> userInfo
    * -> buildingInfo
    * -> buildingImages
    * -> buildingReviews
    */


    public void onCreate(SQLiteDatabase db) {

        //Login Table
        try {
            String loginTable = "CREATE TABLE IF NOT EXISTS login(userID VARCHAR," +
                    "username VARCHAR," +
                    "password VARCHAR) ";
            db.execSQL(loginTable);
            insertLoginData();
        }catch (Exception e){
            Log.i("Exception-loginTable",  e.toString());
        }



        try{
            //User Profile Table
            String userInfoTable = "CREATE TABLE IF NOT EXISTS userInfo(userID VARCHAR," +
                    "firstName VARCHAR," +
                    "lastName VARCHAR)";
            db.execSQL(userInfoTable);
            insertUserInfoData();
        }catch (Exception e){
            Log.i("Exception-userInfoTable",  e.toString());
        }

            //Building Information Table
        try{
            String buildingInfoTable = "CREATE TABLE IF NOT EXISTS buildingInfo(buildingID VARCHAR," +
                    "bName VARCHAR," +
                    "bLocation VARCHAR," +
                    "bCategory INTEGER," +
                    "bPointOne REAL," +
                    "bPointTwo REAL," +
                    "bPointThree REAL," +
                    "bPointFour REAL)";
            db.execSQL(buildingInfoTable);
            insertBuildingInfoData();
        }catch (Exception e){
            Log.i("Exception-BuildingInfo",  e.toString());
        }


            //Building Image Table
        try{
            String buildingImagesTable = "CREATE TABLE IF NOT EXISTS buildingImages(buildingID VARCHAR," +
                    "bImageName VARCHAR)";
            db.execSQL(buildingImagesTable);
            insertBuildingImagesData();
        }catch (Exception e){
            Log.i("Exception-buildingImage",  e.toString());
        }

            //Building Review Table
        try{
            String buildingReviewsTable = "CREATE TABLE IF NOT EXISTS buildingReviews(buildingID VARCHAR," +
                    "userID VARCHAR," +
                    "buildingReview VARCHAR)";
            db.execSQL(buildingReviewsTable);
            insertBuildingReviewsData();
        }catch(Exception e){

            Log.i("Exception-BuildingRevie",  e.toString());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS login");
        db.execSQL("DROP TABLE IF EXISTS userInfo");
        db.execSQL("DROP TABLE IF EXISTS buildingInfo");
        db.execSQL("DROP TABLE IF EXISTS buildingImages");
        db.execSQL("DROP TABLE IF EXISTS buildingReviews");

        // Create tables again in this database
        onCreate(db);

    }



    //Login Data
    public void insertLoginData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO login VALUES('blr1','npari','npari');");
        db.execSQL("INSERT INTO login VALUES('blr2','vsanghvi','vsanghvi');");
        Log.i("LoginData", "Inserted successfully");
    }

    //User Data
    public void insertUserInfoData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO userInfo VALUES('blr1','Nithya','Pari');");
        db.execSQL("INSERT INTO userInfo VALUES('blr2','Viral','Sanghvi');");
        Log.i("UserInfoTable", "Inserted successfully");
    }

        //Building Info Data
    public void insertBuildingInfoData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO buildingInfo VALUES('b1','Flying Saucer','Raleigh Downtown',1,7.840, 4.550, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b2','Lincoln Theatre','Raleigh Downtown',2,8.030, 8.420, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b3','Cafe de Los Muertos','Raleigh Downtown',3,7.450,5.050, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b4','Poole’s Diner','Raleigh Downtown',1,6.150, 6.930, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b5','Raleigh Times','Raleigh Downtown',2,9.770, 6.770, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b6','Askew-Taylor Point','Raleigh Downtown',3,8.000, 3.0030, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b7','Beasley Chicken and Honey','Raleigh Downtown',1,9.500, 7.140, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b8','Bida Manda','Raleigh Downtown',2,10.180, 7.330, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b9','Oakwood Cafe','Raleigh Downtown',3,13.240, 6.830, 0.0, 0.0);");
        db.execSQL("INSERT INTO buildingInfo VALUES('b10','Lincoln Theatre','Raleigh Downtown',1,8.030 , 8.420, 0.0, 0.0);");
        Log.i("BuildingInfoData", "Inserted successfully");
    }

    //Building Image Data
    public void insertBuildingImagesData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO buildingImages VALUES('b7','b7_1');");
        db.execSQL("INSERT INTO buildingImages VALUES('b7','b7_2');");
        Log.i("BuildingImageData", "Inserted successfully");
    }

    //Building Review Data
    public void insertBuildingReviewsData(){
       SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("INSERT INTO buildingReviews VALUES('b7','blr1','Beasley Chicken and Honey is a wonderful place to have dinner with family');");
       db.execSQL("INSERT INTO buildingReviews VALUES('b7','blr2','I liked the chicken, it was tender and cooked to perfection');");
       Log.i("BuildingReviewData", "Inserted successfully");
    }

    public void logDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor curLogin = db.rawQuery("SELECT username from login", null);
            if (curLogin.moveToFirst()) {
                do {
                    String data = curLogin.getString(curLogin.getColumnIndex("username"));
                    Log.i("login table-username", data+" ");
                } while (curLogin.moveToNext());
            }
            curLogin.close();
        }catch (Exception e){
            Log.i("Exception-logdatabase()",  e.toString());
        }
    }
}

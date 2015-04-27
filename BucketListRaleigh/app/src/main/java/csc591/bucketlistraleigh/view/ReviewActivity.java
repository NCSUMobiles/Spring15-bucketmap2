package csc591.bucketlistraleigh.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;

import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.database.CreateDB;

public class ReviewActivity extends ActionBarActivity {
    private ImageButton btnPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        btnPost = (ImageButton) findViewById(R.id.post);
        btnPost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent mainActivityIntent = new Intent(ReviewActivity.this,HomeActivity.class);
                        //ReviewActivity.this.startActivity(mainActivityIntent);
                        EditText et = (EditText) findViewById(R.id.postReview);
                        String theText = et.getText().toString();
                        Log.i("EditText", theText);
                        File path = getApplication().getDatabasePath("bucketlistraleigh.db");
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(path.getPath(), null,0);
                        db.execSQL("INSERT INTO buildingReviews VALUES('b7','blr1',"+"'"+theText+"'"+");");
                        Log.i("UserReviewData", "Inserted successfully");
                        try {
                            Cursor curLogin = db.rawQuery("SELECT buildingReview from buildingReviews", null);
                            if (curLogin.moveToFirst()) {
                                do {
                                    String data = curLogin.getString(curLogin.getColumnIndex("buildingReview"));
                                    Log.i("buildingReviews", data+" ");
                                } while (curLogin.moveToNext());
                            }
                            curLogin.close();
                        }catch (Exception e){
                            Log.i("Exception-logdb1()",  e.toString());
                        }
                    }
                });
    }
}

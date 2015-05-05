package csc591.bucketlistraleigh.helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;

import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.view.BuildingImageActivity;
import csc591.bucketlistraleigh.view.BuildingReviewActivity;



/**
 * Created by Nupur on 5/4/2015.
 */
public class popup {

    public void displayBuildingInfo(final Activity activity, ImageView view, String bName, String bID){

        //Storing the building ID and building name from the method parameter to pass via the Intent
        final String buildingID = bID;
        final String buildingName = bName;
        final ImageView view1 = view;
        final Activity activity1 = activity;

        LayoutInflater layoutInflater =
                (LayoutInflater)activity.getBaseContext()
                        .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable(activity.getResources(),""));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view, 150, -350);

        //Update the name of the building in the sticky note dynamically using method parameter buildingName
        TextView textOut = (TextView) popupView.findViewById(R.id.buildingDescription);
        textOut.setText(buildingName);

        //Setting the View

        //Making Building Image Button Clickable
        ImageButton buildingPhotoButton = (ImageButton) popupView.findViewById(R.id.photoButton);
        buildingPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(activity, BuildingImageActivity.class);
                imageIntent.putExtra("buildingID",buildingID);
                imageIntent.putExtra("buildingName",buildingName);
                activity.startActivity(imageIntent); //This executes the intent for Photo Button

            }
        });

        //Making Building Review Button Clickable
        ImageButton buildingReviewButton = (ImageButton) popupView.findViewById(R.id.reviewButton);
        buildingReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(v.getContext(), BuildingReviewActivity.class);
                reviewIntent.putExtra("buildingID",buildingID);
                reviewIntent.putExtra("buildingName",buildingName);
                activity.startActivity(reviewIntent); //This executes the intent Review Button
            }
        });

        //Making the Add comment button clickable
        ImageButton addReviewButton = (ImageButton) popupView.findViewById(R.id.add);
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getActivity(), ReviewActivity.class);
                //getActivity().startActivity(intent); //This executes the intent for Photo Button
                displayRevInfo(activity1, view1,buildingName,buildingID);
                Log.i("We are back","we are back");
            }
        });
    }

    private void displayRevInfo(Activity activity, ImageView view, String bName, String bID){

        //Storing the building ID and building name from the method parameter to pass via the Intent
        final String buildingID = bID;
        final String buildingName = bName;

        final Activity activity1 = activity;

        LayoutInflater layoutInflater =
                (LayoutInflater)activity1.getBaseContext()
                        .getSystemService(activity1.LAYOUT_INFLATER_SERVICE);
        final View popupView1 = layoutInflater.inflate(R.layout.activity_review, null);

        final PopupWindow popupWindow1 = new PopupWindow(
                popupView1, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,true);

        popupWindow1.setBackgroundDrawable(new BitmapDrawable(activity1.getResources(),""));
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.showAsDropDown(view, 1200, -400);

        //Update the name of the building in the sticky note dynamically using method parameter buildingName
        //TextView textOut = (TextView) popupView.findViewById(R.id.buildingDescription);
        //textOut.setText(buildingName);
        ImageButton btnPost = (ImageButton) popupView1.findViewById(R.id.post);
        btnPost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent mainActivityIntent = new Intent(ReviewActivity.this,HomeActivity.class);
                        //ReviewActivity.this.startActivity(mainActivityIntent);
                        EditText et = (EditText) popupView1.findViewById(R.id.postReview);
                        String theText = et.getText().toString();
                        Log.i("EditText", theText);
                        Log.i("Building id",buildingID);
                        File path = activity1.getApplication().getDatabasePath("bucketlistraleigh.db");
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(path.getPath(), null,0);
                        db.execSQL("INSERT INTO buildingReviews VALUES("+"'"+buildingID+"'"+",'blr1',"+"'"+theText+"'"+");");
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

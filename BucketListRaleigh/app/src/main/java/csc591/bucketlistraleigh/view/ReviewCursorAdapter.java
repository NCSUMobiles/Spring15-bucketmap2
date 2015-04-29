package csc591.bucketlistraleigh.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import csc591.bucketlistraleigh.R;

/**
 * Created by Nithya Pari on 4/27/2015.
 * This adapter is being created to pull the cursor results from the database and display it in the
 * ListView
 */
public class ReviewCursorAdapter extends CursorAdapter {

    public ReviewCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    //Creating a new view using the new layout file containing just the review textView
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.building_review_content, parent, false);
    }

    //Binding the textView as the user scrolls down the reviews
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView reviewTextOut = (TextView) view.findViewById(R.id.buildingReview);
        String userReview = cursor.getString(cursor.getColumnIndexOrThrow("buildingReview"));
        reviewTextOut.setText(userReview);
    }

    private class ViewHolder {
        private TextView reviewTextOut;

        private ViewHolder(View view) {
            reviewTextOut = (TextView) view.findViewById(R.id.buildingReview);
        }

    }
}
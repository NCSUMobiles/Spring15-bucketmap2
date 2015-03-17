package csc591.bucketlistraleigh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.view.View;

import java.io.InputStream;

public class GifMoviewView extends View {

    private Movie mMovie;
    private long mMovieStart;

    public GifMoviewView(Context context, InputStream stream){
        super(context);
        mMovie = Movie.decodeStream(stream);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        final long now = SystemClock.uptimeMillis();

        if(mMovieStart == 0){
            mMovieStart = now;
        }

        final int relTime = (int)((now - mMovieStart) % mMovie.duration());
        mMovie.setTime(relTime);
        mMovie.draw(canvas,10,10);
        this.invalidate();
    }


}

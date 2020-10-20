package Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import servers.monitor.fastest.hoohoonew.App;

/**
 * Created by soheilmohammadi on 12/20/17.
 */

public class ProgressBarIndicator extends AppCompatTextView {

    private static final String TAG = "ProgressBarIndicator";
    
    private Paint arcPaint;
    private Paint circlePaint;
    private  Random random = new Random();


    private long progress = 0 ;
    private long progressSweep = 0 ;
    private int strokeWidth = 2 ;


    public ProgressBarIndicator(Context context) {
        super(context);
        init();
    }

    public ProgressBarIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressBarIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init() {

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(strokeWidth);
        arcPaint.setStyle(Paint.Style.STROKE);



        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStrokeWidth(strokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
    }



    public void setProgress(long progress) {
        this.progress = progress;
        this.progressSweep = (progress * 360) / 100 ;
        setText(progress + " % ");
        invalidate();
    }


    public long getCurrentProgress() {
        return this.progress ;
    }


    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth ;
        arcPaint.setStrokeWidth(strokeWidth);
        circlePaint.setStrokeWidth(strokeWidth);
    }




    @Override
    protected void onDraw(Canvas canvas) {

        RectF rect = new RectF( strokeWidth, strokeWidth, getWidth() -  strokeWidth  ,
                getHeight()  - strokeWidth );

        int red  = random.nextInt(255) ;
        int green  = random.nextInt(255) ;
        int blue  = random.nextInt(255) ;

        arcPaint.setColor(Color.argb(255, red , green , blue));


        canvas.drawCircle(getWidth()/2  ,  getHeight() /2 , (getWidth()/2 ) - strokeWidth,  circlePaint);
        canvas.drawArc(rect , 0 , this.progressSweep, false , arcPaint );

        super.onDraw(canvas);

        setTypeface(App.getInstance().getAppFont(false));
    }
}

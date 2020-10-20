package Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 10/16/17.
 */

public class CircularTextView extends AppCompatTextView {

    private Paint paint_circle ;
    private int circleColor  = Color.GREEN;

    public CircularTextView(Context context) {
        super(context);
        init(null);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private  void init(AttributeSet attrs) {

        paint_circle = new Paint();
        paint_circle.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_circle.setAntiAlias(true);
        paint_circle.setColor(this.circleColor);

        if(attrs != null ){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularTextView);
            int color_circle =  typedArray.getColor(R.styleable.CircularTextView_circle_color , 0) ;
            paint_circle.setColor(color_circle);

            typedArray.recycle();
        }



    }


    public void setCircleColor(int color) {
        this.circleColor = circleColor ;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(getWidth() != getHeight()) {
            if(getWidth() < getHeight()) {
                canvas.drawCircle(getWidth() /2  , getHeight() /2 ,getWidth() /2  , paint_circle);
            }
            else {
                canvas.drawCircle(getWidth() /2  , getHeight() /2 ,getHeight() /2  , paint_circle);
            }
        }else {
            canvas.drawCircle(getWidth() /2  , getHeight() /2 ,getHeight() /2  , paint_circle);
        }


        super.onDraw(canvas);

    }
}

package Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;


public class CustomTextView extends AppCompatTextView {

    private Boolean isBold  = false ;

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attrs) {

        if(attrs != null) {
            TypedArray typedArray = App.context.obtainStyledAttributes(attrs , R.styleable.CustomTextView);
            isBold = typedArray.getBoolean(R.styleable.CustomTextView_isBold , false);
        }

       setTypeface(App.getInstance().getAppFont(isBold));

    }
}

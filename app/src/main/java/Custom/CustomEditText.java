package Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;


public class CustomEditText extends AppCompatEditText {

    private Boolean isBold  = false ;

    public CustomEditText(Context context) {
        super(context);
        init(null);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attrs) {

        if(attrs != null) {
            TypedArray typedArray = App.context.obtainStyledAttributes(attrs , R.styleable.CustomEditText);
            isBold = typedArray.getBoolean(R.styleable.CustomEditText_isBoldEdt , false);
        }

       setTypeface(App.getInstance().getAppFont(isBold));

    }
}

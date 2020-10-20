package Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;


public class CustomButton extends AppCompatButton {

    private Boolean isBold  = false ;

    public CustomButton(Context context) {
        super(context);
        init(null);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }



    private void init(@Nullable AttributeSet attrs) {

        if(attrs != null) {
            TypedArray typedArray = App.context.obtainStyledAttributes(attrs , R.styleable.CustomButton);
            isBold = typedArray.getBoolean(R.styleable.CustomButton_isBoldButton , false);
        }

       setTypeface(App.getInstance().getAppFont(isBold));

    }
}

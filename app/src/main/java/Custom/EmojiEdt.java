package Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiTextView;

import androidx.annotation.Nullable;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class EmojiEdt extends EmojiEditText {

    private Boolean isBold  = false ;

    public EmojiEdt(Context context) {
        super(context);
        init(null);
    }

    public EmojiEdt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attrs) {

        if(attrs != null) {
            TypedArray typedArray = App.context.obtainStyledAttributes(attrs , R.styleable.CustomEmojiEdt);
            isBold = typedArray.getBoolean(R.styleable.CustomEmojiEdt_isBoldEmojiEdt , false);
        }

        setTypeface(App.getInstance().getAppFont(isBold));

    }
}

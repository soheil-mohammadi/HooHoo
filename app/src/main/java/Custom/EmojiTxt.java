package Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.vanniktech.emoji.EmojiTextView;

import androidx.annotation.Nullable;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class EmojiTxt extends EmojiTextView {

    private Boolean isBold  = false ;

    public EmojiTxt(Context context) {
        super(context);
        init(null);
    }

    public EmojiTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attrs) {

        if(attrs != null) {
            TypedArray typedArray = App.context.obtainStyledAttributes(attrs , R.styleable.CustomEmojiTxt);
            isBold = typedArray.getBoolean(R.styleable.CustomEmojiTxt_isBoldEmojiTxt , false);
        }

        setTypeface(App.getInstance().getAppFont(isBold));
    }
}

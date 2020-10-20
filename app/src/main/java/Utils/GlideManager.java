package Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import Custom.RoundedCornersTransformation;
import androidx.core.content.ContextCompat;
import jp.wasabeef.glide.transformations.BlurTransformation;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class GlideManager {


    private static GlideManager instance ;
    private RequestManager glide ;

    private  final long interval = 3000 * 1000;



    public static GlideManager builder() {

        if(instance == null)
            instance = new GlideManager();

        return instance;
    }


    public GlideManager() {
        this.glide = Glide.with(App.context);
    }


    public void loadVideoFrame(String videoPath , ImageView imageView) {
        glide.asBitmap()
                .load(videoPath)
                .apply(new RequestOptions().frame(interval))
                .into(imageView);
    }

    public void loadVideoFrame(String videoPath , int placeHolder ,ImageView imageView) {
        glide.asBitmap()
                .load(videoPath)
                .placeholder(placeHolder)
                .apply(new RequestOptions().frame(interval))
                .into(imageView);
    }


    public void loadRes(int res , ImageView imageView) {
        glide.load(res).
                apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)).placeholder(R.drawable.logo).into(imageView);
    }


    public void loadBitmap(Bitmap bitmap , ImageView imageView) {
        glide.load(bitmap).placeholder(R.drawable.logo).into(imageView);
    }

    public void loadRes(int res  ,ImageView imageView , int placeHolder) {
        glide.load(res).
                apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)).
                placeholder(placeHolder).into(imageView);
    }


    public void loadBytes(byte[] bytes , ImageView imageView) {
        glide.load(bytes).apply(new RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL))
                .placeholder(R.drawable.logo).into(imageView);
    }


    public void loadBytes(byte[] bytes , int placeHolder , ImageView imageView) {
        glide.load(bytes).apply(new RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL))
                .placeholder(placeHolder).into(imageView);
    }



    public void loadRes(int res ,  int colorPlaceHolder , ImageView imageView) {
        glide.load(res).
                apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .placeholder(new ColorDrawable(ContextCompat
                        .getColor(App.context , colorPlaceHolder))).into(imageView);
    }


    public void loadRes(String resName , ImageView imageView) {

        glide.load(App.context.getResources().getIdentifier(resName , "drawable" ,
                App.context.getPackageName()))
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)).placeholder(R.drawable.logo).into(imageView);
    }



    public void loadResBlur(String resName , ImageView imageView) {
        glide.load(App.context.getResources().getIdentifier(resName , "drawable" ,
                App.context.getPackageName()))
                .placeholder(R.drawable.logo)
                .transform(new BlurTransformation(25))
                .into(imageView);
    }

    public void loadPath(String imgPath , ImageView imageView) {
        glide.load(imgPath).placeholder(R.drawable.logo).into(imageView);
    }


    public void loadPathWithoutCache(String imgPath , ImageView imageView) {
        glide.load(imgPath).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.logo).into(imageView);
    }

    private static final String TAG = "GlideManager";

    public void loadPath(String imgPath , int placeHolder , ImageView imageView) {
        glide.load(imgPath).placeholder(placeHolder).into(imageView);

    }

    public void loadPathInRealSize(String imgPath , int placeHolder , ImageView imageView) {


        glide.load(imgPath).into(imageView).getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {

                imageView.getLayoutParams().width = width;
                imageView.getLayoutParams().height = height;
                imageView.requestLayout();
            }
        });
    }


    public void loadRoundPathInRealSize(String imgPath , int radius , String strokeColor ,
                                        int strokeWidth , ImageView imageView) {


        glide.load(imgPath)
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(App.context,
                                radius, 15,  strokeColor, strokeWidth))).into(imageView).
                getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {

                        imageView.getLayoutParams().width = width;
                        imageView.getLayoutParams().height = height;
                        imageView.requestLayout();
                    }
                });

    }



    public void loadPathBlur(String imgPath , ImageView imageView) {
        glide.load(imgPath).
                transform(new BlurTransformation(25))
                .into(imageView);
    }

    public void loadPathBlurWithoutCache(String imgPath , ImageView imageView) {
        glide.load(imgPath).
                diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new BlurTransformation(25))
                .into(imageView);
    }


    public void loadPathCornerBlurWithoutCache(String imgPath ,  int radius ,ImageView imageView) {


        glide.load(imgPath).
                diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new BlurTransformation(25) ,new RoundedCorners(radius))
                .into(imageView);

    }



    public void loadDrawable(Drawable drawable , ImageView imageView) {
        glide.load(drawable).apply(new RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL))
                .into(imageView);
    }
}

package lv;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import lv.chi.photopicker.loader.ImageLoader;
import servers.monitor.fastest.hoohoonew.R;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void loadImage(@NotNull Context context, @NotNull ImageView view, @NotNull Uri uri) {
        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(view);
    }
}
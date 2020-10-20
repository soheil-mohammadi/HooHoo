package Custom;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import Listeners.OnCustomToolbarListener;
import androidx.appcompat.app.ActionBar;
import de.hdodenhof.circleimageview.CircleImageView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 6/2/18.
 */

public class Toolbar {

    private static final String TAG = "Toolbar";


    private static Toolbar instance ;
    private androidx.appcompat.widget.Toolbar toolbar ;
    private View view ;

    private LinearLayout container_main_toolbar , container_toolbar , container_tools_toolbar;
    private CircleImageView img_main_toolbar ;
    private  ImageView img_tools_one_toolbar , img_tools_two_toolbar;
    private TextView txt_title_toolbar ;
    private Boolean hasNavBar = false;


    public static final  int NO_IMG_TOOL = -1;


    public static Toolbar builder() {
        if(instance == null)
            instance = new Toolbar();
        return  instance;
    }


    public Toolbar view(View view) {
        this.view = view  ;
        initViews();
        return this ;
    }

    public Toolbar view(View view , Boolean hasNavBar) {
        this.view = view  ;
        initViews();
        App.getInstance().getCurrentActivity().setSupportActionBar(toolbar);
        ActionBar actionBar = App.getInstance().getCurrentActivity().getSupportActionBar();
        this.hasNavBar = hasNavBar;
        actionBar.setDisplayHomeAsUpEnabled(hasNavBar);
        actionBar.setDisplayShowHomeEnabled(hasNavBar);
        return this ;
    }

    public Toolbar view(View view , Boolean hasNavBar , int imgtoolOne , int imgToolTwo) {
        this.view = view  ;
        initViews();
        App.getInstance().getCurrentActivity().setSupportActionBar(toolbar);
        ActionBar actionBar = App.getInstance().getCurrentActivity().getSupportActionBar();
        this.hasNavBar = hasNavBar;
        actionBar.setDisplayHomeAsUpEnabled(hasNavBar);
        actionBar.setDisplayShowHomeEnabled(hasNavBar);

        if(imgtoolOne != NO_IMG_TOOL) {
            img_tools_one_toolbar.setVisibility(View.VISIBLE);
            img_tools_one_toolbar.setImageResource(imgtoolOne);
        }

        if(imgToolTwo != NO_IMG_TOOL) {
            img_tools_two_toolbar.setVisibility(View.VISIBLE);
            img_tools_two_toolbar.setImageResource(imgToolTwo);
        }


        return this ;
    }


    private void initViews() {
        toolbar = view.findViewById(R.id.toolbar);
        container_main_toolbar = view.findViewById(R.id.container_main_toolbar);
        container_toolbar = view.findViewById(R.id.container_toolbar);
        container_tools_toolbar = view.findViewById(R.id.container_tools_toolbar);
        img_main_toolbar = view.findViewById(R.id.img_main_toolbar);
        img_tools_one_toolbar = view.findViewById(R.id.img_tools_one_toolbar);
        img_tools_two_toolbar = view.findViewById(R.id.img_tools_two_toolbar);
        txt_title_toolbar = view.findViewById(R.id.txt_title_toolbar);
    }


    public Toolbar backColor(int color) {
        container_toolbar.setBackgroundColor(color);
        return this ;
    }

    public Toolbar backDrawable(int drawableRes) {
        container_toolbar.setBackgroundResource(drawableRes);
        return this ;
    }


    public Toolbar title(String title) {
        if (title != null && !title.trim().equals(""))
            txt_title_toolbar.setText(title);
        else
            txt_title_toolbar.setVisibility(View.GONE);

        return this ;
    }

    public String getTitle() {
        return txt_title_toolbar.getText().toString() ;
    }


    public ImageView getMainImage() {
        return img_main_toolbar ;
    }



    public Toolbar image (Bitmap toolbarImage) {
        if (toolbarImage != null){
            img_main_toolbar.setImageBitmap(toolbarImage);
        }else {
            img_main_toolbar.setVisibility(View.GONE);
        }

        return this ;
    }

    public Toolbar image (int resToolbarImage) {
        if (resToolbarImage != NO_IMG_TOOL){
            img_main_toolbar.setImageResource(resToolbarImage);
        }else {
            img_main_toolbar.setVisibility(View.GONE);
        }

        return this ;
    }


    public void build (final OnCustomToolbarListener listener) {

        listener.handle_toolbar_component_(txt_title_toolbar , img_main_toolbar ,
                img_tools_one_toolbar , img_tools_two_toolbar);
        if(hasNavBar) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.on_navBar_clicked();
                }
            });
        }
    }

}

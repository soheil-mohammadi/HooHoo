package servers.monitor.fastest.hoohoonew;


import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.service.notification.StatusBarNotification;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import Components.DaggerAppComponent;
import Fragments.BaseDialogFragment;
import Fragments.BaseFragment;
import Listeners.OnAlertActionsClicked;
import Listeners.OnCustomClick;
import Listeners.OnCustomDialog;
import Listeners.OnReadyRetrofit;
import Listeners.OnThread;
import Models.FabricEventNumModel;
import Models.FabricEventStrModel;
import Modules.AppModule;
import Utils.SharedManager;
import activities.IntroActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.fabric.sdk.android.Fabric;
import life.sabujak.roundedbutton.RoundedButton;
import lv.GlideImageLoader;
import lv.chi.photopicker.ChiliPhotoPicker;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Math.abs;


/**
 * Created by soheilmohammadi on 9/4/18.
 */

public class App extends Application {

    private  final String TAG = "App";
    public static Context context;



    public static File WORKPATH ;

    private static App instance ;

    public static final String BASE_URL_MAIN = "http://fr.web-app-pro.com";
    public  static final String BASE_URL_MAIN_1 = "http://us.web-app-pro.com";

    private static AppCompatActivity currentActivity ;

    @Inject
    public App app;

    public Retrofit retrofit;

    @Inject
    public   AppDatabase appDatabase;



    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();

        WORKPATH = new File( context.getFilesDir().getAbsolutePath() + "/HooHooNew");

        DaggerAppComponent.builder().appModule(new AppModule(this , context))
                .build().inject(this);

        instance = app;

        ChiliPhotoPicker.init(new GlideImageLoader(), "hoohoo.new.photoProvider");
        EmojiManager.install(new IosEmojiProvider());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }



    public void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    public String getISP(){

        boolean isOnWifi = false;

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm!= null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            if(connManager != null) {
                NetworkInfo mWifi = connManager.getActiveNetworkInfo();
                if (mWifi != null && mWifi.getType() == ConnectivityManager.TYPE_WIFI) isOnWifi = true ;
            }

            String operatorName = "None";

            if(tel != null) {
                operatorName = tel.getSimOperatorName();
            }

            if(isOnWifi) {
                return "Wifi";
            }else {
                return operatorName;
            }


        }

        return "Wifi";
    }




    public void setCurrentLang(String lang) {
        SharedManager.builder().set("lang", lang);
        App.currentActivity.finish();
        App.currentActivity.startActivity(new Intent(App.currentActivity , IntroActivity.class));

    }

    public String getCurrentLang() {
        return  SharedManager.builder().get("lang" , "en");
    }


    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Configuration config  ;
            if(currentActivity != null) {
                config = currentActivity.getResources().getConfiguration();
            }else  {
                config = context.getResources().getConfiguration();
            }

            Locale.setDefault(myLocale);
            config.setLocale(myLocale);

            if(currentActivity != null) {
                currentActivity.createConfigurationContext(config);
                currentActivity.getResources().updateConfiguration(config,
                        currentActivity.getResources().getDisplayMetrics());

            }else {
                context.createConfigurationContext(config);
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }

        }else  {
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.setLocale(myLocale);
            res.updateConfiguration(conf, dm);
        }

    }



    public String calculateBetweenDate(long date) {
        long between_current_and_chat_time = abs(System.currentTimeMillis() - date);
        if (between_current_and_chat_time > 1000 * 60 * 60 * 24) {
            return new JalaliCalendar().getJalaliDate(new Date(date));
        } else {
            if (Math.floor(between_current_and_chat_time / 1000) < 60) {
                return   getResString(R.string.lable_ago_secs , between_current_and_chat_time / (1000));
            } else if (Math.floor(between_current_and_chat_time / (1000 * 60)) < 60) {
                return  getResString(R.string.lable_ago_mins , between_current_and_chat_time / (1000 * 60));
            } else {
                return  getResString(R.string.lable_ago_hours , between_current_and_chat_time / (1000 * 60 * 60));
            }
        }
    }



    public String readFileFromAssets(String name) throws Exception {
        BufferedReader b=new BufferedReader(new InputStreamReader(App.context.getAssets().open(name)));
        String ret="";
        try{
            for(;;){
                String s=b.readLine();
                if(s==null) break;
                ret+=s;
            }
        }catch(EOFException e){}
        return ret;
    }

    public String getResString(int res_id) {
        return currentActivity.getString(res_id );
    }

    public String getResString(int res_id , Object... formatArgs ) {
        return currentActivity.getString(res_id  , formatArgs);
    }



    private void submitNewEvent(String eventName , ArrayList<FabricEventStrModel> strAttrs ,
                                ArrayList<FabricEventNumModel> numAttrs){


        CustomEvent customEvent = new CustomEvent(eventName);

        Bundle bundle = new Bundle();

        if(strAttrs != null && strAttrs.size()> 0 ) {
            for (FabricEventStrModel strAttr: strAttrs) {
                bundle.putString(strAttr.getAttrName(), strAttr.getValue());
                customEvent.putCustomAttribute(strAttr.getAttrName() , strAttr.getValue());
            }
        }

        if(numAttrs != null && numAttrs.size() > 0) {
            for (FabricEventNumModel numAttr: numAttrs) {
                bundle.putInt(numAttr.getAttrName(), numAttr.getValue());
                customEvent.putCustomAttribute(numAttr.getAttrName() , numAttr.getValue());
            }
        }

        Answers.getInstance().logCustom(customEvent);

    }

    public void pushFabricEvent(String eventName , Object... objects) {

        ArrayList<FabricEventStrModel> strAttrs = new ArrayList<>();
        ArrayList<FabricEventNumModel> numAttrs = new ArrayList<>();

        if(objects != null) {

            for (int i = 0; i <objects.length ; i++) {
                Object obj = objects[i];

                if(obj instanceof FabricEventStrModel)
                    strAttrs.add((FabricEventStrModel) obj);

                if(obj instanceof  FabricEventNumModel)
                    numAttrs.add((FabricEventNumModel) obj);

            }
        }

        strAttrs.add(new FabricEventStrModel("VersionName" ,  BuildConfig.VERSION_NAME + ""));
        strAttrs.add(new FabricEventStrModel("DeviceName" , Build.BRAND + "-" + Build.CPU_ABI ));
        strAttrs.add(new FabricEventStrModel("AndroidVersion" , Build.VERSION.RELEASE + ""));
        strAttrs.add(new FabricEventStrModel("InternetService" , getISP() ));

        submitNewEvent(eventName + "-v1" , strAttrs  , numAttrs);

    }

    public int getVersionCode() {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return  pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0 ;
        }
    }


    public String getVersionName() {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return  pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0" ;
        }
    }



    public void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            App.currentActivity.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            showCustomToast(context.getString(R.string.not_found_act_for_handle) , Toast.LENGTH_SHORT
                    , R.color.colorRed);
        }
    }


    public String getBaseUrl() {
        return BASE_URL_MAIN ;
    }



    public void setCurrentActivity(AppCompatActivity activity) {
        currentActivity = activity;
    }

    public AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }

    public Context getContext() {
        return context;
    }

    public String getBASE_URL_MAIN() {
        return BASE_URL_MAIN;
    }



    public static App getInstance() {
        return instance;
    }

    public void removeAllFrgs() {

        FragmentManager fm = currentActivity.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }



    public void showPrgDialog(String title , String desc , boolean isCancelable , OnCustomDialog onCustomDialog) {

        showCustomDialog(R.layout.dialog_progress, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, isCancelable, false, new OnCustomDialog() {
                    @Override
                    public void doSomeThing(Dialog dialog) {
                        TextView txt_prg_title = dialog.findViewById(R.id.txt_prg_title);
                        TextView txt_prg_des = dialog.findViewById(R.id.txt_prg_des);

                        txt_prg_title.setText(title);
                        txt_prg_des.setText(desc);
                        onCustomDialog.doSomeThing(dialog);
                    }
                });

    }
    public void showCustomDialog(int resLayout , int width , int height, Boolean isCancelable , Boolean isHide ,
                                 OnCustomDialog onCustomDialog) {
        Dialog dialog = new Dialog(getCurrentActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(resLayout);
        dialog.getWindow().setLayout(width , height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(isCancelable);
        onCustomDialog.doSomeThing(dialog);
        if(isHide)
            dialog.hide();
        else
            try{
                dialog.show();
            }catch (WindowManager.BadTokenException e) {

            }
    }


    public String calculateTimeUntilNow(long time) {

        if(time == 0 ) {
            return  "00 : 00 : 00 ";
        }else  {
            long diffSecs = (System.currentTimeMillis() - time) / (1000);

            if(diffSecs <= 60) {
                return  " 00 " + " : " + " 00 " + " : " + (diffSecs % 60)   ;
            }else if(diffSecs < 60 * 60){
                return  " 00 " + " : " +  ((diffSecs / (60)) % 60)  + " : " + (diffSecs % 60) ;
            }else if(diffSecs < 24 * 60 * 60) {
                return  ((diffSecs / (60*60)) % 24) + "  : " +
                        + ((diffSecs / (60)) % 60) + " : " + (diffSecs % 60) ;
            }else {
                return  ((diffSecs / (24 * 60*60))) +  " : " +
                        ((diffSecs / (60*60)) % 24) + " : " +
                        + ((diffSecs / (60)) % 60) + " : " + (diffSecs % 60) ;
            }
        }

    }


    public String calculateMinUntilNow(long time) {

        if(time == 0 ) {
            return  "0";
        }else  {
            long diffSecs = (System.currentTimeMillis() - time) / (1000);

            if(diffSecs < 60 * 60){
                return  ((diffSecs / (60)) % 60)  + "" ;
            }else {
                return "0";
            }
        }

    }


    public Boolean isNotificationVisible(int notId){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(mNotificationManager != null ) {
            StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
            for (StatusBarNotification notification : notifications) {
                if (notification.getId() == notId) {
                    return true;
                }
            }

            return  false;
        }else  {
            return false;
        }

    }


    public void showFrg(int idResFrame , Boolean isReplace , Fragment fragment) {
        if(currentActivity !=null) {
            try {
                FragmentManager fragmentManager = currentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(isReplace) {
                    fragmentTransaction.replace(idResFrame , fragment);
                }else {
                    Fragment currentFrg = (Fragment) getCurrentFrg(R.id.frame_activity_main);
                    if(currentFrg != null) {
                        fragmentTransaction.hide(currentFrg);
                    }
                    fragmentTransaction.add(idResFrame ,fragment , null);
                }

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }catch (IllegalStateException e) {

            }

        }

    }


    public void showDialogFrg( BaseDialogFragment dialogFragment) {
        if(currentActivity !=null) {
            try {
                FragmentManager fragmentManager = currentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);
                dialogFragment.show(fragmentTransaction, "dialog");

            }catch (IllegalStateException e) {

            }

        }
    }

    public void hideDialogFrg(BaseDialogFragment dialogFragment) {
        if(currentActivity !=null) {
            try {
                FragmentManager fragmentManager = currentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.hide(dialogFragment);

            }catch (IllegalStateException e) {

            }

        }
    }

    public Object getCurrentFrg(int idResFrame) {
        if(currentActivity != null) {
            FragmentManager fragmentManager = currentActivity.getSupportFragmentManager();
            if(fragmentManager.findFragmentById(idResFrame) instanceof BaseFragment)
                return (BaseFragment) fragmentManager.findFragmentById(idResFrame);
            else if(fragmentManager.findFragmentById(idResFrame) instanceof BaseDialogFragment)
                return (BaseDialogFragment) fragmentManager.findFragmentById(idResFrame);

        }

        return  null;
    }


    public int getRandom(int min , int max) {
        return new Random().nextInt((max - min)) + min;
    }




    public AppDatabase getAppDatabase() {
        return appDatabase;
    }


    public void showSnackbar(View containerView, String text , int duration
            , int txtColor) {
        Snackbar snackbar = Snackbar.make(containerView, "", duration);
        int marginFromSides = 15;

        View snackView = currentActivity.getLayoutInflater().inflate(R.layout.snackbar_layout, null);
        snackbar.getView().setBackgroundColor(Color.WHITE);

        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
        parentParams.setMargins(marginFromSides, 0, marginFromSides, marginFromSides);
        parentParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        parentParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        snackBarView.setLayoutParams(parentParams);

        TextView textView = snackView.findViewById(R.id.txt_custom_snack_bar);
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(context , txtColor));
        snackBarView.addView(snackView, 0);
        snackbar.show();
    }

    public void showCustomToast(String text , int duration , int colorBack) {
        Toast toast = Toast.makeText(context ,  text , duration);
        View toastView = toast.getView();
        toastView.setBackgroundColor(ContextCompat.getColor(context , colorBack));
        TextView txtView = toastView.findViewById(android.R.id.message);
        txtView.setTypeface(getAppFont(false));
        txtView.setPadding(5 , 5 , 5 , 5);
        txtView.setTextColor(Color.WHITE);
        toast.show();
    }


    public Typeface getAppFont(Boolean isBold) {
        if(App.getInstance().getCurrentLang().equals("en"))
            return ResourcesCompat.getFont(App.context, isBold ?  R.font.en_bold : R.font.en);
        else
            return ResourcesCompat.getFont(App.context, isBold ? R.font.isans_bold : R.font.isans);
    }


    public void getRetrofit(String url  , final OnReadyRetrofit onReadyRetrofit) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(6, TimeUnit.SECONDS)
                .writeTimeout(6 , TimeUnit.SECONDS)
                .connectTimeout(6, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();


        onReadyRetrofit.onReady(retrofit);
    }



    public String getUniqeId() {
        return ""+
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 ;
    }

    public long getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC +03:30"));
        Date currentLocalTime = cal.getTime();
        return  currentLocalTime.getTime();
    }



    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;

    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    public void cancelNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager !=null)
            notificationManager.cancel(id);
    }

    public void loadWithGlide(String url , ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder( R.mipmap.ic_launcher);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(url).into(imageView);
    }


    public void setLastSelectedServer(int serverId  , String flagAddrImg , String serverName ,
                                      String serverNameReq) {
        SharedManager.builder().set("serverId" , serverId);
        SharedManager.builder().set("serverImg" ,  flagAddrImg);
        SharedManager.builder().set("serverNameShow" , serverName.contains("@") ?
                serverName.split("@")[0] : serverName);
        SharedManager.builder().set("serverNameReq" , serverNameReq);
    }


    public Boolean isDeviceLock() {
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM != null  && myKM.inKeyguardRestrictedInputMode()) {
            return true;
        }

        return  false;
    }




    public void showAlertDialogBuilder( String title , String msg ,
                                        final OnAlertActionsClicked onAlertActionsClicked)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onAlertActionsClicked.onNegativeClicked(dialog);
            }
        });
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onAlertActionsClicked.onPositiveClicked(dialog);

            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.setIcon( R.mipmap.ic_launcher);
        builder.show();
    }


    public void  shareApk() {

        String filePath ;

        ApplicationInfo app = context.getApplicationInfo();
        filePath = app.sourceDir;


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("application/vnd.android.package-archive");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        currentActivity.startActivity(Intent.createChooser(intent, context.getString(R.string.hoohoo)));

    }


    public void shareTextPlain(String subject , String shareBody) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        currentActivity.startActivity(Intent.createChooser(sharingIntent,
                getResString(R.string.share_using)));

    }





    public String getNewBaseUrl(String baseUrl ) {

        String baseResult  ;

        switch (baseUrl) {

            case BASE_URL_MAIN :
                baseResult = BASE_URL_MAIN_1;
                break;



            default:
                baseResult = null;
                break;
        }

        return baseResult;
    }


    public Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;

    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        return false;

    }

    public boolean isNoCalling() {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager != null) {
            return  telephonyManager.getCallState() == TelephonyManager.CALL_STATE_IDLE;
        }

        return true;

    }



    public boolean isUserRated() {
        int userConnectedCount = SharedManager.builder().get("UserConnectedCount" , 0);

        if(userConnectedCount >= 3)
            return  SharedManager.builder().get("UserRated" , false);
        else
            return true;
    }


    public void sendGmail() {

        try {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + "mad4r20@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, currentActivity.getString(R.string.report_issue_of_app_name));
            currentActivity.startActivity(Intent.createChooser(emailIntent, "Send email using..."));

        } catch (android.content.ActivityNotFoundException ex) {
            showCustomToast("There are no email clients installed" , Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }

    public void setUserRated() {
        SharedManager.builder().set("UserRated" , true);
    }

    public void initThread(final OnThread onThread) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                onThread.onReady();
            }
        });

        thread.start();
    }


    public void setIntroPassed() {
        SharedManager.builder().set("IntroPassedSoheil" , true);
    }

    public boolean isIntroPassed() {
        return SharedManager.builder().get("IntroPassedSoheil" , false);
    }

    public String getExtPath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HooHooNew" ;

        File file = new File(path);
        if(!file.exists()) {
            file.mkdir();
        }

        return path + "/";
    }


}

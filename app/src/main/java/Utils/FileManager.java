package Utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import core.Patterns;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class FileManager {

    private static final String TAG = "FileManager";

    private static FileManager instance ;

    public static FileManager builder() {
        if(instance == null)
            instance = new FileManager();

        return instance;
    }

    public void openDocFile(Uri uri) {
        String extention = Patterns.builder().getMimeType(uri);

        switch (extention) {

            case ".pdf" :
                openPdfFile(uri);
                break;

            case ".docx" :
                openWordFile(uri);
                break;

            case ".txt" :
                openTextFile(uri);
                break;

            case ".xlsx" :
                openExcelFile(uri);
                break;

            case ".pptx" :
                openPowerPointFile(uri);
                break;

        }

    }


    public enum DocumentType {

        PDF ,
        POWER_POINT ,
        EXCEL ,
        WORD ,
        TEXT

    }




    public int getDocLogo(String filePath) {
        String extention = Patterns.builder().getMimeType(Uri.parse(filePath));

        switch (extention) {

            case ".pdf" :
                return R.drawable.ic_pdf;

            case ".docx" :
                return R.drawable.ic_word;

            case ".txt" :
                return R.drawable.ic_text;

            case ".xlsx" :
                return R.drawable.ic_excel;

            case ".pptx" :
                return R.drawable.ic_power_point;

        }

        return 0 ;
    }

    public void openImgFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }



    public void openVideoFile(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filePath));
        intent.setDataAndType(Uri.parse(filePath), "video/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }

    public void openAudioFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }


    public void openApkFile(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }


    public void openPowerPointFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/vnd.ms-powerpoint");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }

    public void openPdfFile(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }


    public void openWordFile(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/msword");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }


    public void openExcelFile(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }


    public void openTextFile(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            App.context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            App.getInstance().showCustomToast(App.getInstance().getResString(R.string.no_app_found_to_handle_intent) ,
                    Toast.LENGTH_SHORT , R.color.colorPrimaryRed);
        }
    }
}

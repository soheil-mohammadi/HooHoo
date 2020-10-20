package FrgPresenter;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import Fragments.BaseDialogFragment;
import FrgView.BaseDialogFrgView;
import FrgView.RecieverModeDialogView;
import Listeners.OnHotspotReady;
import Utils.GlideManager;
import androidx.core.content.ContextCompat;
import core.ConnectionManager;
import core.types.HotspotManager;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;

public class RecieverModeDialogPresenter extends BaseDialogFrgPresenter implements OnHotspotReady , View.OnClickListener {

    private  final String TAG = "RecieverModeDialogPresenter";
    private Handler handler;

    private boolean isShowedBarcodeInfo = false;
    private String infoForBarcode ;


    @Override
    public void onCreate(BaseDialogFragment fragment, View view, BaseDialogFrgView viewArc) {
        super.onCreate(fragment, view, viewArc);
        handler = new Handler(Looper.getMainLooper());
        HotspotManager.builder().start(this);
        initViews();
    }


    private void initViews() {
        ((RecieverModeDialogView) viewArc).getBtn_cancel_recieve_mode_dialog().setOnClickListener(this::onClick);
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    public void onDismissed() {
        if(!ConnectionManager.isConnected()) {
            HotspotManager.builder().clear();
            HotspotManager.builder().unRegisterServerReciever();
        }

    }


    @Override
    public void onStarted(String ssid , String password) {

        infoForBarcode = ssid + "#" + password;

        String str = App.getInstance().getResString(R.string.discovrable_by_name , ssid) ;
        Spannable ssidSpan = new SpannableString(str);
        ssidSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(App.context , R.color.colorPrimaryGreenDark))
                , 24 ,  str.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((RecieverModeDialogView) viewArc).getTxt_state_recieve_mode_dialog().setText(ssidSpan);
        ((RecieverModeDialogView) viewArc).getTxt_waiting_recieve_mode_dialog().setVisibility(View.VISIBLE);

        HotspotManager.builder().registerServerReciever();


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((RecieverModeDialogView) viewArc).getImg_barcode_generator_recieve_mode_dialog()
                    .setVisibility(View.VISIBLE);
            ((RecieverModeDialogView) viewArc).getImg_barcode_generator_recieve_mode_dialog()
                    .setOnClickListener(this::onClick);
        }

    }

    @Override
    public void onFailed() {
        ((RecieverModeDialogView) viewArc).getTxt_state_recieve_mode_dialog().setText(
                App.getInstance().getResString(R.string.initializing_for_reciever_mode));
        ((RecieverModeDialogView) viewArc).getTxt_waiting_recieve_mode_dialog().setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_cancel_recieve_mode_dialog :
                cancelRecieveMode();
                break;


            case R.id.img_barcode_generator_recieve_mode_dialog :
                generateWifiHotspotInfoByBarcode();
                break;
        }
    }

    private void generateWifiHotspotInfoByBarcode() {

        if(isShowedBarcodeInfo) {
            isShowedBarcodeInfo = false;
            ((RecieverModeDialogView) viewArc).getImg_barcode_recieve_mode_dialog().setVisibility(View.GONE);

        } else {

            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(infoForBarcode, BarcodeFormat.QR_CODE, 150, 150);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                int [] pixels = new int [width * height];
                for (int i = 0 ; i < height ; i++)
                {
                    for (int j = 0 ; j < width ; j++)
                    {
                        if (bitMatrix.get(j, i))
                        {
                            pixels[i * width + j] = 0xff000000;
                        }
                        else
                        {
                            pixels[i * width + j] = 0xffffffff;
                        }
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                ((RecieverModeDialogView) viewArc).getImg_barcode_recieve_mode_dialog().setVisibility(View.VISIBLE);
                GlideManager.builder().loadBitmap(bitmap , ((RecieverModeDialogView) viewArc).getImg_barcode_recieve_mode_dialog());
                isShowedBarcodeInfo = true;
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }


    }

    private void cancelRecieveMode() {
        fragment.dismiss();
    }
}

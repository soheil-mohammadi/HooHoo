package FrgPresenter;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.barcode.scanner.overlay.BarcodeOverlay;
import com.google.android.gms.vision.barcode.Barcode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Adapter.ChatsFakeSearchingAdapter;
import Adapter.WifiDeviceAdapter;
import Fragments.BaseDialogFragment;
import FrgView.BaseDialogFrgView;
import FrgView.DiscoverDialogView;
import Listeners.OnConnectionState;
import Listeners.OnCustomClick;
import Listeners.OnDeviceNearbyFound;
import Models.ChatsFakeModel;
import Models.WifiDevice;
import Utils.PermissionManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import core.LocationManager;
import core.types.HotspotManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import servers.monitor.fastest.hoohoonew.App;
import core.ConnectionManager;
import servers.monitor.fastest.hoohoonew.R;

public class DiscoverDialogPresenter extends BaseDialogFrgPresenter  implements OnConnectionState ,
        OnDeviceNearbyFound{

    private static final String TAG = "DiscoverDialogPresenter";
    private boolean isConnecting = false;
    private boolean foundAnyDevice = false;
    private Handler handler;

    private  Disposable barcodeScanner ;

    private  Timer fakeChatSmoothTimer ;

    private WifiDevice targetDevice;


    @Override
    public void onCreate(BaseDialogFragment fragment, View view, BaseDialogFrgView viewArc) {
        super.onCreate(fragment, view, viewArc);
        handler = new Handler(Looper.getMainLooper());
        ConnectionManager.builder().setNotify(this);
        startDiscovering();
        initFakeChatPersons();
        handleTryAgainBtn();
        handleCancelBtn();
    }


    private void handleTryAgainBtn() {
        ((DiscoverDialogView) viewArc).getBtn_again_search_chats_available().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAgain();
            }
        });

        ((DiscoverDialogView) viewArc).getBtn_grant_camera_permission_discover_dialog().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grantCameraPermission();
            }
        });
    }

    private void handleCancelBtn() {
        ((DiscoverDialogView) viewArc).getBtn_cancel_discover_dialog().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.dismiss();
            }
        });
    }

    private void startDiscovering() {

        ((DiscoverDialogView) viewArc).getContainer_searching_chats_available().setVisibility(View.VISIBLE);
        ((DiscoverDialogView) viewArc).getContainer_not_found_searching_chats_available().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getContainer_chats_available().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getContainer_not_found_searching_chats_available().setVisibility(View.GONE);

        HotspotManager.builder().start(this);

    }


    private void prepareDeviceFounded(ArrayList<WifiDevice> targets) {
        ((DiscoverDialogView) viewArc).getRecycler_chats_available()
                .setAdapter(new WifiDeviceAdapter(targets, new OnCustomClick() {
                            @Override
                            public void onClicked(Object object) {
                                targetDevice = (WifiDevice) object;
                                isConnecting = true;
                                ConnectionManager.builder().connect(targetDevice);
                            }
                        })
                );
    }




    private void setFakeChatSmoothTimer(ArrayList<Integer> resBitmaps) {

        if(fakeChatSmoothTimer == null) {
            fakeChatSmoothTimer = new Timer();
            fakeChatSmoothTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((DiscoverDialogView) viewArc).getRecycler_preview_chats_searching().smoothScrollToPosition(0);
                    for (int i = 0; i < resBitmaps.size(); i++) {
                        try {
                            final int finalI = i;
                            App.getInstance().getCurrentActivity().runOnUiThread(() -> {
                                ((DiscoverDialogView) viewArc).getRecycler_preview_chats_searching().smoothScrollToPosition(finalI);
                            });

                            Thread.sleep(500);

                            if (foundAnyDevice)
                                break;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } , 0 ,500);

        }else {
            cancelFakeChatSmoothTimer();
            setFakeChatSmoothTimer(resBitmaps);
        }

    }


    private void cancelFakeChatSmoothTimer () {
        if(fakeChatSmoothTimer != null) {
            fakeChatSmoothTimer.cancel();
            fakeChatSmoothTimer = null;
        }
    }


    private void initFakeChatPersons() {
        ArrayList<Integer> resBitmaps = ChatsFakeModel.builder().list();

        ((DiscoverDialogView) viewArc).getRecycler_preview_chats_searching().setLayoutManager(
                new GridLayoutManager(fragment.getContext(), 1,
                        RecyclerView.HORIZONTAL, false));

        ((DiscoverDialogView) viewArc).getRecycler_preview_chats_searching().setAdapter(
                new ChatsFakeSearchingAdapter(resBitmaps));

        setFakeChatSmoothTimer(resBitmaps);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void searchAgain() {
        startDiscovering();
    }

    @Override
    public void onConnected() {
        ConnectionManager.builder().removeNotify(this);
        //ConnectionManager.builder().redirectToChat();
    }

    @Override
    public void onConnecting() {
        ((DiscoverDialogView) viewArc).getContainer_barcode_scanner_discover_dialog().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getTxt_not_found_key_device_discover_dialog().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getContainer_connecting_people().setVisibility(View.VISIBLE);
        ((DiscoverDialogView) viewArc).getContainer_chats_available().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getTxt_connecting_to_device_discover_dialog().setText(
                App.getInstance().getResString(R.string.connecting_to_device, targetDevice.Name));
    }


    @Override
    public void onDisconnected() {

    }

    @Override
    public void onFailed(int reason) {
        isConnecting = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                App.getInstance().showCustomToast(App.getInstance().getResString(R.string.connection_failed)
                        , Toast.LENGTH_SHORT, R.color.colorRed);
                ((DiscoverDialogView) viewArc).getContainer_barcode_scanner_discover_dialog().setVisibility(View.GONE);
                ((DiscoverDialogView) viewArc).getContainer_connecting_people().setVisibility(View.GONE);
                startDiscovering();
            }
        });

    }

    @Override
    public void notFoundKey() {
        Log.e(TAG, "notFoundKey: !!" );
        ((DiscoverDialogView) viewArc).getTxt_not_found_key_device_discover_dialog().setVisibility(View.VISIBLE);
        ((DiscoverDialogView) viewArc).getContainer_connecting_people().setVisibility(View.VISIBLE);
        ((DiscoverDialogView) viewArc).getContainer_chats_available().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getTxt_connecting_to_device_discover_dialog().setText(
                App.getInstance().getResString(R.string.connecting_to_device, targetDevice.Name));
    }


    @Override
    public void onBarcodeScan() {
        Log.e(TAG, "onBarcodeScan: !! ") ;
        ((DiscoverDialogView) viewArc).getTxt_not_found_key_device_discover_dialog().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getContainer_connecting_people().setVisibility(View.GONE);
        ((DiscoverDialogView) viewArc).getContainer_barcode_scanner_discover_dialog().setVisibility(View.VISIBLE);
        handleBarcodeScanner();
    }


    private void grantCameraPermission() {
        PermissionManager.builder().grant(PermissionManager.CAMERA, new PermissionManager.OnPermissionResult() {
            @Override
            public void onAccepted() {
                //Log.e(TAG, "onAccepted: !!!" );
                ((DiscoverDialogView) viewArc).getContainer_grant_camera_permission_discover_dialog()
                        .setVisibility(View.GONE);
                initBarcodeScannerView();

            }

            @Override
            public void onDeny() {
                //Log.e(TAG, "onDeny: !!!" );
            }
        });
    }

    private void handleBarcodeScanner() {

        if(!PermissionManager.builder().isGranted(PermissionManager.CAMERA)) {
            ((DiscoverDialogView) viewArc).getContainer_grant_camera_permission_discover_dialog()
                    .setVisibility(View.VISIBLE);

        }else {
            initBarcodeScannerView();
        }


    }



    private void initBarcodeScannerView() {
        barcodeScanner = ((DiscoverDialogView) viewArc).getBarcodeView_discover_dialog()
                .drawOverlay(null)
                .setAutoFocus(true)
                .getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Barcode>() {
                    @Override
                    public void accept(Barcode barcode) throws Exception {
                        Log.e(TAG, "accept: " + barcode.displayValue );
                        HotspotManager.builder().connectWithBarcode(barcode.displayValue);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public void onDismissed() {

        if(barcodeScanner != null)
            barcodeScanner.dispose();

        ConnectionManager.builder().removeNotify(this);
        cancelFakeChatSmoothTimer();
        if(!ConnectionManager.isConnected()) {
            HotspotManager.builder().clear();
        }

    }


    @Override
    public void onFound(ArrayList<WifiDevice> devices) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (!isConnecting) {
                    ((DiscoverDialogView) viewArc).getContainer_not_found_searching_chats_available().setVisibility(View.GONE);
                    ((DiscoverDialogView) viewArc).getContainer_searching_chats_available().setVisibility(View.GONE);
                    ((DiscoverDialogView) viewArc).getContainer_chats_available().setVisibility(View.VISIBLE);
                    ((DiscoverDialogView) viewArc).getRecycler_chats_available().setLayoutManager(
                            new GridLayoutManager(fragment.getContext(), 2,
                                    RecyclerView.VERTICAL, false));
                    prepareDeviceFounded(devices);

                }

            }
        });
    }


    @Override
    public void notFound() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ((DiscoverDialogView) viewArc).getContainer_searching_chats_available().setVisibility(View.GONE);
                ((DiscoverDialogView) viewArc).getContainer_not_found_searching_chats_available().setVisibility(View.VISIBLE);
            }
        });
    }
}

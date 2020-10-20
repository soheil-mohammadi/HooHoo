package FrgView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.barcode.scanner.BarcodeView;

import FrgPresenter.DiscoverDialogPresenter;
import androidx.recyclerview.widget.RecyclerView;
import life.sabujak.roundedbutton.RoundedButton;
import servers.monitor.fastest.hoohoonew.R;

public class DiscoverDialogView extends BaseDialogFrgView {


    private LinearLayout container_connecting_people  ,
            container_searching_chats_available ,
            container_not_found_searching_chats_available , container_chats_available ,
            container_barcode_scanner_discover_dialog , container_grant_camera_permission_discover_dialog;

    private TextView txt_connecting_to_device_discover_dialog , txt_not_found_key_device_discover_dialog ;
    private RecyclerView recycler_preview_chats_searching , recycler_chats_available;

    private BarcodeView barcodeView_discover_dialog ;

    private RoundedButton btn_again_search_chats_available  , btn_cancel_discover_dialog
            , btn_grant_camera_permission_discover_dialog;

    @Override
    public int getLayout() {
        return R.layout.discover_dialog;
    }

    @Override
    public void initViews() {
        container_connecting_people = view.findViewById(R.id.container_connecting_people);
        container_searching_chats_available = view.findViewById(R.id.container_searching_chats_available);
        container_chats_available = view.findViewById(R.id.container_chats_available);
        container_barcode_scanner_discover_dialog = view.findViewById(R.id.container_barcode_scanner_discover_dialog);
        recycler_preview_chats_searching = view.findViewById(R.id.recycler_preview_chats_searching);
        container_not_found_searching_chats_available = view.findViewById(R.id.container_not_found_searching_chats_available);
        container_grant_camera_permission_discover_dialog = view.findViewById(R.id.container_grant_camera_permission_discover_dialog);
        txt_connecting_to_device_discover_dialog = view.findViewById(R.id.txt_connecting_to_device_discover_dialog);
        txt_not_found_key_device_discover_dialog = view.findViewById(R.id.txt_not_found_key_device_discover_dialog);
        recycler_chats_available = view.findViewById(R.id.recycler_chats_available);
        btn_again_search_chats_available = view.findViewById(R.id.btn_again_search_chats_available);
        btn_cancel_discover_dialog = view.findViewById(R.id.btn_cancel_discover_dialog);
        btn_grant_camera_permission_discover_dialog = view.findViewById(R.id.btn_grant_camera_permission_discover_dialog);
        barcodeView_discover_dialog = view.findViewById(R.id.barcodeView_discover_dialog);
    }

    @Override
    public void doSomeThing() {

    }


    public LinearLayout getContainer_grant_camera_permission_discover_dialog() {
        return container_grant_camera_permission_discover_dialog;
    }


    public RoundedButton getBtn_grant_camera_permission_discover_dialog() {
        return btn_grant_camera_permission_discover_dialog;
    }

    public LinearLayout getContainer_barcode_scanner_discover_dialog() {
        return container_barcode_scanner_discover_dialog;
    }


    public BarcodeView getBarcodeView_discover_dialog() {
        return barcodeView_discover_dialog;
    }


    public TextView getTxt_connecting_to_device_discover_dialog() {
        return txt_connecting_to_device_discover_dialog;
    }

    public LinearLayout getContainer_chats_available() {
        return container_chats_available;
    }

    public RecyclerView getRecycler_chats_available() {
        return recycler_chats_available;
    }

    public LinearLayout getContainer_connecting_people() {
        return container_connecting_people;
    }

    public LinearLayout getContainer_searching_chats_available() {
        return container_searching_chats_available;
    }

    public LinearLayout getContainer_not_found_searching_chats_available() {
        return container_not_found_searching_chats_available;
    }

    public RecyclerView getRecycler_preview_chats_searching() {
        return recycler_preview_chats_searching;
    }

    public RoundedButton getBtn_again_search_chats_available() {
        return btn_again_search_chats_available;
    }

    public RoundedButton getBtn_cancel_discover_dialog() {
        return btn_cancel_discover_dialog;
    }

    public TextView getTxt_not_found_key_device_discover_dialog() {
        return txt_not_found_key_device_discover_dialog;
    }

    @Override
    public void onClick(View view) {
        //TODO
    }
}

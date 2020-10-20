package Sheets;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Listeners.OnCustomClick;
import core.MsgType;
import core.Patterns;
import androidx.annotation.Nullable;
import servers.monitor.fastest.hoohoonew.R;

public class AttachFilesSheet extends BottomSheetDialogFragment implements View.OnClickListener{

    private static final String TAG = "BottomSheet3DialogFragm";

    private OnCustomClick onCustomClick;


    public static AttachFilesSheet newInstance(OnCustomClick onCustomClick) {
        Bundle args = new Bundle();
        AttachFilesSheet fragment = new AttachFilesSheet(onCustomClick);
        fragment.setArguments(args);
        return fragment;
    }


    public AttachFilesSheet(OnCustomClick onCustomClick) {
        this.onCustomClick = onCustomClick;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        //super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.dialog_attach_files, null);
        dialog.setContentView(contentView);

        final LinearLayout container_gallery_dialog_attach_files = dialog.findViewById(R.id.container_gallery_dialog_attach_files);
        final LinearLayout container_audio_dialog_attach_files = dialog.findViewById(R.id.container_audio_dialog_attach_files);
        final LinearLayout container_video_dialog_attach_files = dialog.findViewById(R.id.container_video_dialog_attach_files);
        final LinearLayout container_apk_dialog_attach_files = dialog.findViewById(R.id.container_apk_dialog_attach_files);
        final LinearLayout container_document_dialog_attach_files = dialog.findViewById(R.id.container_document_dialog_attach_files);


        container_gallery_dialog_attach_files.setOnClickListener(this::onClick);
        container_audio_dialog_attach_files.setOnClickListener(this::onClick);
        container_video_dialog_attach_files.setOnClickListener(this::onClick);
        container_apk_dialog_attach_files.setOnClickListener(this::onClick);
        container_document_dialog_attach_files.setOnClickListener(this::onClick);



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.container_gallery_dialog_attach_files :
                onCustomClick.onClicked(MsgType.Image);
                dismiss();
                break;

            case R.id.container_audio_dialog_attach_files :
                onCustomClick.onClicked(MsgType.Audio);
                dismiss();
                break;

            case R.id.container_video_dialog_attach_files :
                onCustomClick.onClicked(MsgType.Video);
                dismiss();
                break;

            case R.id.container_apk_dialog_attach_files :
                onCustomClick.onClicked(MsgType.APK);
                dismiss();
                break;

            case R.id.container_document_dialog_attach_files :
                onCustomClick.onClicked(MsgType.Document);
                dismiss();
                break;


        }
    }
}
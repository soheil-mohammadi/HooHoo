package FrgPresenter;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.greentoad.turtlebody.docpicker.DocPicker;
import com.greentoad.turtlebody.docpicker.core.DocPickerConfig;
import com.greentoad.turtlebody.mediapicker.MediaPicker;
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Adapter.ChatAdapter;
import Custom.Toolbar;
import Dialogs.WarningDialog;
import Entities.ChatContentEntity;
import Entities.ChatHeaderEntity;
import Entities.UserEntity;
import Fragments.BaseFragment;
import Fragments.ChatFrg;
import Fragments.ChatProfileDialogFrg;
import Fragments.InstalledAppsFrg;
import Fragments.MainFrg;
import FrgView.BaseFrgView;
import FrgView.ChatView;
import Listeners.OnConnectionState;
import Listeners.OnCustomClick;
import Listeners.OnCustomDialog;
import Listeners.OnReadMsg;
import Listeners.OnSendMsg;
import Models.AppModel;
import Models.ChatValueModel;
import Models.ChatsFakeModel;
import ViewModels.SelectedAppsViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import asyncTasks.ChatDataSaver;
import core.ChatDataController;
import core.types.HotspotManager;
import core.MsgType;
import core.Patterns;
import Sheets.AttachFilesSheet;
import core.RecieveMsg;
import core.SendMsg;
import core.SerializeObject;
import core.models.FileModel;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lv.chi.photopicker.PhotoPickerFragment;
import servers.monitor.fastest.hoohoonew.App;
import core.ConnectionManager;
import DateController.ChatContentManager;
import DateController.ChatHeaderManager;
import Utils.GlideManager;
import servers.monitor.fastest.hoohoonew.R;


public class ChatPresenter extends BaseFrgPresenter implements
        OnConnectionState , OnSendMsg , OnReadMsg {


    private static final String TAG = "ChatPresenter";
    private EmojiPopup emojiPopup ;

    private UserEntity recieverUserInfo  = null;

    private String lastMsg = "...";

    private Dialog prgPreparingDialog;

    private Handler handler;


    private UserEntity userInfo ;

    private boolean isInitedChatListUi  = false;

    private ChatAdapter chatAdapter = null ;

    private ArrayList<ChatValueModel> chatData = new ArrayList<>();

    private String onlineUniqueID = null;
    private String offlineUniqueID = null;


    @Override
    public void onCreate(BaseFragment fragment) {
        super.onCreate(fragment);
        userInfo = App.getInstance().getAppDatabase().userDao().fetchUser();
        if(fragment.getArguments() != null) {
            offlineUniqueID = fragment.getArguments().getString(ChatFrg.offlineKey, null);
        }
        setSelectionApps();
    }


    private void setSelectionApps() {
        SelectedAppsViewModel selectedAppsViewModel =  ViewModelProviders.of(App.getInstance().getCurrentActivity())
                .get(SelectedAppsViewModel.class);

        selectedAppsViewModel.onSelectedApps().observe(fragment, new androidx.lifecycle.Observer<ArrayList<AppModel>>() {
            @Override
            public void onChanged(ArrayList<AppModel> apps) {
                if(apps.size() > 0) {
                    ArrayList<Uri> appUris = new ArrayList<>();
                    for (AppModel app: apps) {
                        appUris.add(Uri.parse(app.getApkPath()));
                    }
                    apksPicked(appUris);
                    selectedAppsViewModel.clearAllData();
                }

            }
        });
    }


    @Override
    public void onCreateView(BaseFragment fragment, View view, BaseFrgView viewArc) {
        super.onCreateView(fragment, view, viewArc);
        handler = new Handler(Looper.getMainLooper());
        if(offlineUniqueID == null)
            showPrgPreparingDialog();

        GlideManager.builder().loadRes(R.drawable.chat_background , R.color.colorGray,
                ((ChatView)viewArc).getImg_background_chat_frg());
        setupChat();
        handleTextChanged();

    }

    private void showPrgPreparingDialog() {
        App.getInstance().showPrgDialog(App.getInstance().getResString(R.string.preparing_chat),
                App.getInstance().getResString(R.string.preparing_chat_desc), false, new OnCustomDialog() {
                    @Override
                    public void doSomeThing(Dialog dialog) {
                        prgPreparingDialog = dialog;
                    }
                });


        setTimeoutGettingInfo();

    }



    private void setTimeoutGettingInfo() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(recieverUserInfo == null) {
                    if(prgPreparingDialog != null) {
                        prgPreparingDialog.dismiss();
                    }

                    ConnectionManager.builder().disconnect(false);
                }
            }
        } , 18000);
    }

    private void handleTextChanged() {
        ((ChatView)viewArc).getEdt_txt_frg_chat().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!((ChatView)viewArc).getMessageText().equals("")) {
                    ((ChatView)viewArc).getAttachment_chat_files().setVisibility(View.GONE);
                    ((ChatView)viewArc).getImg_send_txt_chat().setVisibility(View.VISIBLE);
                }else {
                    ((ChatView)viewArc).getAttachment_chat_files().setVisibility(View.VISIBLE);
                    ((ChatView)viewArc).getImg_send_txt_chat().setVisibility(View.GONE);
                }
            }

        });
    }

    private void setupChat() {

        if(offlineUniqueID == null) {
            ConnectionManager.builder().setNotify(this);
            SendMsg.builder().setNotify(this);
            RecieveMsg.builder().setNotify(this);
            SendMsg.builder().sendUserInfo(userInfo);
            ((ChatView)viewArc).getContainer_no_chats_available().setVisibility(View.VISIBLE);
            ((ChatView)viewArc).getRecycler_chat_frg().setVisibility(View.GONE);

        }else {
            reloadHistory(offlineUniqueID );
        }

    }


    private void reloadHistory(String uniqueID ) {

        if(uniqueID != null) {
            ArrayList<ChatContentEntity> chatContent =  ChatContentManager.builder().getAllChats(uniqueID);

            if(chatContent != null && chatContent.size() > 0) {
                ((ChatView)viewArc).getContainer_no_chats_available().setVisibility(View.GONE);
                ((ChatView)viewArc).getRecycler_chat_frg().setVisibility(View.VISIBLE);
                ((ChatView)viewArc).getRecycler_chat_frg().setLayoutManager(new LinearLayoutManager(App.context));
                chatData = convertChatContent(chatContent);
                chatAdapter = new ChatAdapter(chatData);
                ((ChatView)viewArc).getRecycler_chat_frg().setAdapter(chatAdapter);
                ((ChatView)viewArc).getRecycler_chat_frg().scrollToPosition(chatData.size() -1);
            }

            ChatHeaderEntity chatHeader =  ChatHeaderManager.builder().getChat(uniqueID);

            if(chatHeader != null) {
                recieverUserInfo = new UserEntity(chatHeader.chatWithName , chatHeader.bio,
                        chatHeader.avatar  , chatHeader.uniqueID , chatHeader.isMale);
                Toolbar.builder().title(recieverUserInfo.getUserName());
                setUserAvatar();
            }


        }

    }


    private void setUserAvatar() {
        if(recieverUserInfo.getUserAvatar().equals("")) {

            String res = ChatsFakeModel.builder().getRandomAvatarName(
                    recieverUserInfo.getUserSex() == 1);

            GlideManager.builder().loadRes(res,
                    Toolbar.builder().getMainImage());

        } else {

            GlideManager.builder().loadPathWithoutCache(recieverUserInfo.getUserAvatar()
                    ,  Toolbar.builder().getMainImage());
        }
    }


    private ArrayList<ChatValueModel> convertChatContent(ArrayList<ChatContentEntity> chatContent ) {

        ArrayList<ChatValueModel> result = new ArrayList<>();

        for (ChatContentEntity content : chatContent) {
            result.add(new ChatValueModel(content.getRowID() , content.getMsgType() == MsgType.Text ?
                    content.getValue() : SerializeObject.builder().stringToObject(content.getValue())
                    ,content.isSender() , content.getTime() , content.getMsgType()));

        }

        return result ;
    }



    @Override
    public void onResume() {
        super.onResume();
        //TODO
    }

    public void onEmojiClicked() {

        if(emojiPopup == null) {

            emojiPopup = EmojiPopup.Builder.fromRootView(((ChatView) viewArc).getContainer_frg_chat())
                    .build(((ChatView) viewArc).getEdt_txt_frg_chat());
            App.getInstance().hideKeyboard(view);
            emojiPopup.toggle();
            ((ChatView) viewArc).getEmoji_frg_chat().setImageResource(R.drawable.ic_keyboard);

        }else {
            if(emojiPopup.isShowing()) {
                App.getInstance().openKeyboard();
                emojiPopup.dismiss();
                ((ChatView) viewArc).getEmoji_frg_chat().setImageResource(R.drawable.ic_emoji);
            }else {
                App.getInstance().hideKeyboard(view);
                emojiPopup.toggle();
                ((ChatView) viewArc).getEmoji_frg_chat().setImageResource(R.drawable.ic_keyboard);
            }
        }

    }

    public void onBackPressed() {

        if(offlineUniqueID == null)
            ConnectionManager.builder().disconnect(true);
        else {
            SendMsg.builder().destroy();
            RecieveMsg.builder().destroy();
            fragment.destroyFrg();
        }
    }


    public void onNavBackPressed() {

        if(offlineUniqueID == null)
            ConnectionManager.builder().disconnect(true);
        else {
            SendMsg.builder().destroy();
            RecieveMsg.builder().destroy();
            fragment.destroyFrg();
        }
    }

    public void onAttachFilesClicked() {

        BottomSheetDialogFragment bottomSheetDialogFragment =
                AttachFilesSheet.newInstance(new OnCustomClick() {
                    @Override
                    public void onClicked(Object object) {
                        MsgType msgType =  (MsgType) object ;

                        switch (msgType) {

                            case Image:
                                PhotoPickerFragment.newInstance(true ,true,40 ,
                                        R.style.ChiliPhotoPicker_Dark).show(App.getInstance()
                                                .getCurrentActivity().getSupportFragmentManager(),
                                        "HooHooImagePicker");
                                break;

                            case Video:
                                presentVideoPicker();
                                break;


                            case Audio:
                                presentAudioPicker();
                                break;


                            case APK:
                                presentApkPicker();
                                break;


                            case Document:
                                presentDocumentPicker();
                                break;

                        }
                    }
                });

        bottomSheetDialogFragment.show(App.getInstance().getCurrentActivity().getSupportFragmentManager(),
                bottomSheetDialogFragment.getTag());
    }

    private void presentApkPicker() {
        App.getInstance().showFrg(R.id.frame_activity_main , false ,
                InstalledAppsFrg.newInstance());
    }

    private void presentDocumentPicker() {
        ArrayList<String> docs = new ArrayList<>();
        docs.add(DocPicker.DocTypes.PDF);
        docs.add(DocPicker.DocTypes.MS_POWERPOINT);
        docs.add(DocPicker.DocTypes.MS_EXCEL);
        docs.add(DocPicker.DocTypes.TEXT);
        docs.add(DocPicker.DocTypes.MS_WORD);

        DocPickerConfig pickerConfig = new DocPickerConfig()
                .setAllowMultiSelection(true)
                .setShowConfirmationDialog(true)
                .setExtArgs(docs);

        DocPicker.with(App.getInstance().getCurrentActivity())
                .setConfig(pickerConfig)
                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        SendMsg.builder().sendFile(MsgType.Document , uris , offlineUniqueID == null);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });

    }

    private void presentAudioPicker() {
        MediaPickerConfig pickerConfig = new MediaPickerConfig()
                .setAllowMultiSelection(true)
                .setUriPermanentAccess(true)
                .setShowConfirmationDialog(true)
                .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MediaPicker.with(App.getInstance().getCurrentActivity()
                ,MediaPicker.MediaTypes.AUDIO)
                .setConfig(pickerConfig)
                .setFileMissingListener(new MediaPicker.MediaPickerImpl.OnMediaListener() {
                    @Override
                    public void onMissingFileWarning() {
                        //trigger when some file are missing
                    }
                })
                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        SendMsg.builder().sendFile(MsgType.Audio , uris , offlineUniqueID == null);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }


    private void presentVideoPicker() {
        MediaPickerConfig pickerConfig = new MediaPickerConfig()
                .setAllowMultiSelection(true)
                .setUriPermanentAccess(true)
                .setShowConfirmationDialog(true)
                .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MediaPicker.with(App.getInstance().getCurrentActivity()
                ,MediaPicker.MediaTypes.VIDEO)
                .setConfig(pickerConfig)
                .setFileMissingListener(new MediaPicker.MediaPickerImpl.OnMediaListener() {
                    @Override
                    public void onMissingFileWarning() {
                        //trigger when some file are missing
                    }
                })
                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        //uris: list of uri
                        SendMsg.builder().sendFile(MsgType.Video , uris , offlineUniqueID == null);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }



    private void updateChatRowUi(ChatValueModel chatValue) {

        if(!isInitedChatListUi && chatAdapter == null) {

            isInitedChatListUi = true;
            ChatDataController.builder().addAll(chatData);
            chatData = ChatDataController.builder().getAll();
            chatAdapter = new ChatAdapter(chatData) ;

            if(chatData.size() > 0) {
                chatAdapter.notifyDataSetChanged();
            }else {
                ((ChatView)viewArc).getContainer_no_chats_available().setVisibility(View.GONE);
                ((ChatView)viewArc).getRecycler_chat_frg().setVisibility(View.VISIBLE);
                ((ChatView)viewArc).getRecycler_chat_frg().setLayoutManager(new LinearLayoutManager(App.context));
                ((ChatView)viewArc).getRecycler_chat_frg().setAdapter(chatAdapter);
                ((ChatView)viewArc).getRecycler_chat_frg().scrollToPosition(chatData.size() -1);
            }


        } else {
            if(!isInitedChatListUi) {
                isInitedChatListUi = true;
                ChatDataController.builder().addAll(chatData);
            }
        }


        boolean isAdded = ChatDataController.builder().add(chatValue);


        int position = ChatDataController.builder()
                .getPosition(chatValue.getUniqueID()) ;

        if(isAdded) {
            chatData.add(chatValue);
            chatAdapter.notifyItemInserted(position);
            ((ChatView)viewArc).getRecycler_chat_frg().scrollToPosition(position);
        }else {
            chatData.set(position , chatValue);
            chatAdapter.notifyItemChanged(position);
        }

    }


    public void imagesPicked(ArrayList<Uri> uris) {
        SendMsg.builder().sendFile(MsgType.Image , uris , offlineUniqueID == null);
    }


    private void apksPicked(ArrayList<Uri> apps) {
        SendMsg.builder().sendFile(MsgType.APK , apps , offlineUniqueID == null);
    }

    @Override
    public void onConnected() {
        //TODO
    }

    @Override
    public void onConnecting() {
        //TODO
    }

    @Override
    public void onDisconnected() {
        fragment.isActionDone = true;
        destroy();
    }


    private void destroy() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                HotspotManager.builder().clear();

                if(chatData.size() > 0) {
                    new ChatDataSaver(chatData , onlineUniqueID  , lastMsg , recieverUserInfo).execute();
                }


                if(prgPreparingDialog != null)
                    prgPreparingDialog.dismiss();

                SendMsg.builder().removeNotify(ChatPresenter.this);
                RecieveMsg.builder().removeNotify(ChatPresenter.this);
                RecieveMsg.builder().destroy();
                SendMsg.builder().destroy();
                ConnectionManager.builder().removeNotify(ChatPresenter.this);
                ChatDataController.builder().destroy();

                try {
                    App.getInstance().removeAllFrgs();
                    App.getInstance().showFrg(R.id.frame_activity_main , true , MainFrg.newInstance());
                    App.getInstance().showCustomToast(App.getInstance().getResString(R.string.connection_disconnected) ,
                            Toast.LENGTH_SHORT , R.color.colorBlue);
                }catch (IllegalStateException e) {

                }
            }
        });


    }

    @Override
    public void onFailed(int reason) {
        //TODO
    }

    @Override
    public void notFoundKey() {
        //TODO
    }

    @Override
    public void onBarcodeScan() {
        //TODO
    }

    @Override
    public void onSendText(String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                lastMsg = message ;
                updateChatRowUi(new ChatValueModel(Patterns.builder().generateUniqueId() , message ,
                        true , System.currentTimeMillis() , MsgType.Text));
            }
        });
    }

    @Override
    public void onSendImg(ArrayList<FileModel> imgs) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel img : imgs) {
                    updateChatRowUi(new ChatValueModel(img.getUniqueID() , img ,
                            true , System.currentTimeMillis() , MsgType.Image));
                }

            }
        });
    }

    @Override
    public void onSendAudio(ArrayList<FileModel> audios) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel audio : audios) {
                    updateChatRowUi(new ChatValueModel(audio.getUniqueID() , audio ,
                            true , System.currentTimeMillis() , MsgType.Audio));
                }

            }
        });
    }


    @Override
    public void onSendVideo(ArrayList<FileModel> videos) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel video : videos) {
                    updateChatRowUi(new ChatValueModel(video.getUniqueID() , video ,
                            true , System.currentTimeMillis() , MsgType.Video));
                }
            }
        });
    }

    @Override
    public void onSendApk(ArrayList<FileModel> apks) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel apk : apks) {
                    updateChatRowUi(new ChatValueModel(apk.getUniqueID() , apk ,
                            true , System.currentTimeMillis() , MsgType.APK));
                }
            }
        });
    }


    @Override
    public void onSendDocument(ArrayList<FileModel> documents) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel document : documents) {
                    updateChatRowUi(new ChatValueModel(document.getUniqueID() , document ,
                            true , System.currentTimeMillis() , MsgType.Document));
                }
            }
        });
    }


    @Override
    public void onSendPrgImg(FileModel img, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatImg =  ChatDataController.builder().get(img.getUniqueID()) ;
                if(chatImg != null) {
                    chatImg.setValue(img);
                    updateChatRowUi(chatImg);

                    if(img.getProgress() >= 100)
                        lastMsg = "Image";
                }

            }
        });
    }

    @Override
    public void onSendPrgVideo(FileModel video, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatVideo =  ChatDataController.builder().get(video.getUniqueID()) ;
                if(chatVideo != null) {
                    chatVideo.setValue(video);
                    updateChatRowUi(chatVideo);

                    if(video.getProgress() >= 100)
                        lastMsg = "Video";
                }

            }
        });
    }

    @Override
    public void onSendPrgApk(FileModel apk, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatApk =  ChatDataController.builder().get(apk.getUniqueID()) ;

                if(chatApk != null) {
                    chatApk.setValue(apk);
                    updateChatRowUi(chatApk);

                    if(apk.getProgress() >= 100)
                        lastMsg = "App";

                }

            }
        });
    }

    @Override
    public void onSendPrgAudio(FileModel audio, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatAudio =  ChatDataController.builder().get(audio.getUniqueID()) ;
                if(chatAudio != null) {
                    chatAudio.setValue(audio);
                    updateChatRowUi(chatAudio);

                    if(audio.getProgress() >= 100)
                        lastMsg = "Audio";

                }

            }
        });
    }

    @Override
    public void onSendPrgDocument(FileModel document, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatDocument =  ChatDataController.builder().get(document.getUniqueID()) ;

                if(chatDocument != null) {
                    chatDocument.setValue(document);
                    updateChatRowUi(chatDocument);

                    if(document.getProgress() >= 100)
                        lastMsg = "Document";

                }

            }
        });
    }

    @Override
    public void onSendNoSpace(long needBytes) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                WarningDialog.builder().show(App.getInstance().getResString(R.string.storage_needed) ,
                        App.getInstance().getResString(
                                R.string.send_need_mb_space, Toolbar.builder().getTitle()  ,
                                Patterns.builder().bytesToMb(needBytes)),
                        R.raw.storage, new OnCustomClick() {
                            @Override
                            public void onClicked(Object object) {

                            }
                        } , null , false);
            }
        });

    }


    @Override
    public void onReceiveUserInfo(UserEntity userInfo) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "User Info is Here !!" );
                recieverUserInfo = userInfo;
                onlineUniqueID = userInfo.getUniqueID();
                recieverUserInfo.setUserAvatar("");
                Toolbar.builder().title(userInfo.getUserName());
                GlideManager.builder().loadRes(ChatsFakeModel.builder().getRandomAvatarName(userInfo.getUserSex() == 1) ,
                        Toolbar.builder().getMainImage());

                reloadHistory(userInfo.uniqueID);

            }
        });
    }


    @Override
    public void onReceiveUserAvatar(FileModel avatar) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // Log.e(TAG, "onReceiveUserAvatar: " + recieverUserInfo );
               // App.getInstance().showCustomToast("Avatar Recieved !" , Toast.LENGTH_SHORT , R.color.colorGreen);

                if(recieverUserInfo != null)
                    recieverUserInfo.setUserAvatar(avatar.getFilePath());

                GlideManager.builder().loadPathWithoutCache(avatar.getFilePath()
                        ,Toolbar.builder().getMainImage());

                if(prgPreparingDialog != null) {
                    prgPreparingDialog.dismiss();
                    prgPreparingDialog = null;
                }


                if(!HotspotManager.isServer)
                    SendMsg.builder().sendUserAvatarByClient(Uri.parse(userInfo.getUserAvatar()) , userInfo.uniqueID);

            }
        } , 1200);
    }

    @Override
    public void onReceiveText(String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                lastMsg = message;
                updateChatRowUi(new ChatValueModel(Patterns.builder().generateUniqueId() , message.trim() ,
                        false , System.currentTimeMillis() , MsgType.Text));
            }
        });
    }

    @Override
    public void onReceiveImg(ArrayList<FileModel> imgs) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel img : imgs) {
                    updateChatRowUi(new ChatValueModel(img.getUniqueID() , img ,
                            false , System.currentTimeMillis() , MsgType.Image));
                }
            }
        });
    }

    @Override
    public void onReceiveAudio(ArrayList<FileModel> audios) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel audio : audios) {
                    updateChatRowUi(new ChatValueModel(audio.getUniqueID() , audio ,
                            false , System.currentTimeMillis() , MsgType.Audio));
                }

            }
        });
    }

    @Override
    public void onReceiveVideo(ArrayList<FileModel> videos) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel video : videos) {
                    updateChatRowUi(new ChatValueModel(video.getUniqueID() , video ,
                            false , System.currentTimeMillis() , MsgType.Video));
                }

            }
        });
    }

    @Override
    public void onReceiveApk(ArrayList<FileModel> apks) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel apk : apks) {
                    updateChatRowUi(new ChatValueModel(apk.getUniqueID() , apk ,
                            false , System.currentTimeMillis() , MsgType.APK));
                }

            }
        });
    }

    @Override
    public void onReceiveDocument(ArrayList<FileModel> documents) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (FileModel document : documents) {
                    updateChatRowUi(new ChatValueModel(document.getUniqueID() , document ,
                            false , System.currentTimeMillis() , MsgType.Document));
                }

            }
        });
    }

    @Override
    public void onReceivePrgImg(FileModel img, long progress) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatImg =  ChatDataController.builder().get(img.getUniqueID()) ;
                chatImg.setValue(img);
                updateChatRowUi(chatImg);

                if(img.getProgress() == 100)
                    lastMsg = "Image";
            }
        });
    }

    @Override
    public void onReceivePrgVideo(FileModel video, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatVideo =  ChatDataController.builder().get(video.getUniqueID()) ;
                chatVideo.setValue(video);
                updateChatRowUi(chatVideo);

                if(video.getProgress() == 100)
                    lastMsg = "Video";
            }
        });
    }

    @Override
    public void onReceivePrgApk(FileModel apk, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatApk =  ChatDataController.builder().get(apk.getUniqueID()) ;
                chatApk.setValue(apk);
                updateChatRowUi(chatApk);

                if(apk.getProgress() == 100)
                    lastMsg = "App";
            }
        });
    }

    @Override
    public void onReceivePrgAudio(FileModel audio, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatAudio =  ChatDataController.builder().get(audio.getUniqueID()) ;
                chatAudio.setValue(audio);
                updateChatRowUi(chatAudio);

                if(audio.getProgress() == 100)
                    lastMsg = "Audio";
            }
        });
    }

    @Override
    public void onReceivePrgDocument(FileModel document, long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatValueModel chatDocument =  ChatDataController.builder().get(document.getUniqueID()) ;
                chatDocument.setValue(document);
                updateChatRowUi(chatDocument);

                if(document.getProgress() == 100)
                    lastMsg = "Document";
            }
        });
    }

    @Override
    public void onReceiveNoSpace(long needBytes) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                WarningDialog.builder().show(App.getInstance().getResString(R.string.storage_needed) ,
                        App.getInstance().getResString(
                                R.string.reciever_need_mb_space, Patterns.builder().bytesToMb(needBytes) ),
                        R.raw.storage, new OnCustomClick() {
                            @Override
                            public void onClicked(Object object) {

                            }
                        } , null , false);
            }
        });

    }

    public void onSendMsgClicked() {
        SendMsg.builder().sendText(((ChatView)viewArc).getMessageText() , offlineUniqueID == null);
        ((ChatView)viewArc).getEdt_txt_frg_chat().setText("");
    }

    public void onReback() {
        destroy();
    }

    public void showProfile() {
        if(recieverUserInfo != null) {
            App.getInstance().showDialogFrg(ChatProfileDialogFrg.newInstance(recieverUserInfo));
        }

    }
}

package Adapter;

import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jorgecastillo.FillableLoader;
import com.vanniktech.emoji.EmojiTextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Custom.ProgressBarIndicator;
import Listeners.OnThread;
import Models.ChatValueModel;
import Utils.FileManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import core.ApkMsg;
import core.models.FileModel;
import info.abdolahi.CircularMusicProgressBar;
import servers.monitor.fastest.hoohoonew.App;
import Utils.GlideManager;
import servers.monitor.fastest.hoohoonew.JalaliCalendar;
import servers.monitor.fastest.hoohoonew.R;

/**
 * Created by soheilmohammadi on 9/18/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TxtViewType = 0 ;
    private final int ImgViewType = 1 ;
    private final int AudioViewType = 2 ;
    private final int VideoViewType = 3;
    private final int ApkViewType = 4;
    private final int DocumentViewType = 5 ;

    private static final String TAG = "ChatAdapter";

    private ArrayList<ChatValueModel> data ;


    private Handler handler;


    public ChatAdapter(ArrayList<ChatValueModel> data ) {
        this.data = data ;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {

        switch (viewType) {

            case  TxtViewType :
                return  new TxtViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_txt_row ,
                        parent , false));


            case ImgViewType :
                return  new ImgViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_img_row ,
                        parent , false));


            case VideoViewType :
                return  new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_video_row ,
                        parent , false));

            case ApkViewType :
                return  new ApkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_apk_row ,
                        parent , false));


            case AudioViewType :
                return  new AudioViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_audio_row ,
                        parent , false));


            case DocumentViewType :
                return  new DocumentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_document_row ,
                        parent , false));


        }

        return null;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final ChatValueModel chat = data.get(position);


        switch (chat.getMsgType()) {

            case Text:
                handleTxtMsg((TxtViewHolder) holder, chat);
                break;


            case Image:
                handleImgMsg((ImgViewHolder) holder, chat);
                break;


            case Video:
                handleVideoMsg((VideoViewHolder) holder, chat);
                break;


            case Audio:
                handleAudioMsg((AudioViewHolder) holder, chat);
                break;


            case APK:
                handleApkMsg((ApkViewHolder) holder, chat);
                break;


            case Document:
                handleDocumentMsg((DocumentViewHolder) holder, chat);
                break;


        }

    }

    private void handleTxtMsg(TxtViewHolder holder, ChatValueModel chat) {

        holder.txt_row_chat.setText((String) chat.getValue());
        App.getInstance().initThread(new OnThread() {
            @Override
            public void onReady() {
                String date = new JalaliCalendar().getJalaliDate(new Date(chat.getTime()));

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.txt_row_time_msg.setVisibility(View.VISIBLE);
                        holder.txt_row_time_msg.setText(date);
                    }
                });

            }
        });

        holder.container_txt_row_chat.setGravity(chat.isSender() ? Gravity.END : Gravity.START);
        holder.txt_row_chat.setBackgroundResource(chat.isSender() ?
                R.drawable.flat_red_box : R.drawable.flat_red_dark_box);

    }


    private void handleImgMsg(ImgViewHolder holder, ChatValueModel chat) {

        FileModel imgFile = (FileModel) chat.getValue();


        if(imgFile.getProgress() == 100) {

            holder.container_img_complete_row_chat.setVisibility(View.VISIBLE);
            holder.prg_img_row_chat.setVisibility(View.GONE);

            GlideManager.builder().loadRoundPathInRealSize(imgFile.getFilePath()  , 15,
                    "#C51856", 3,
                    holder.img_complete_row_chat);


            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {
                    String date = new JalaliCalendar().getJalaliDate(new Date(chat.getTime()));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            holder.txt_img_row_time_msg.setVisibility(View.VISIBLE);
                            holder.txt_img_row_time_msg.setText(date);
                        }
                    });

                }
            });


        } else {

            holder.container_img_complete_row_chat.setVisibility(View.GONE);
            holder.prg_img_row_chat.setVisibility(View.VISIBLE);

            if(chat.isSender())
                GlideManager.builder().loadPath(imgFile.getFilePath() , R.drawable.ic_gallery ,
                        holder.prg_img_row_chat);

            holder.prg_img_row_chat.setValue(imgFile.getProgress());

        }

        holder.container_img_row_chat.setGravity(chat.isSender() ? Gravity.END : Gravity.START);
        holder.container_img_complete_row_chat.setGravity(chat.isSender() ? Gravity.END : Gravity.START);
        holder.img_complete_row_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.builder().openImgFile(chat.isSender() ? Uri.parse(imgFile.getFilePath())
                        : Uri.fromFile(new File(imgFile.getFilePath())));
            }
        });
    }


    private void handleAudioMsg(AudioViewHolder holder, ChatValueModel chat) {

        FileModel audioFile = (FileModel) chat.getValue();

        if(audioFile.getProgress() == 100)
            holder.progress_bar_audio_file.setVisibility(View.GONE);
        else {
            holder.progress_bar_audio_file.setVisibility(View.VISIBLE);
            holder.progress_bar_audio_file.setProgress(audioFile.getProgress());
        }

        holder.container_audio_play_row_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(audioFile.getProgress() == 100)
                    FileManager.builder().openAudioFile(
                            chat.isSender() ? Uri.parse(audioFile.getFilePath())
                                    : Uri.fromFile(new File(audioFile.getFilePath())));
            }
        });


        if(audioFile.getProgress() == 100) {

            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {

                    MediaMetadataRetriever metaRetriver = null;
                    try {
                        metaRetriver = new MediaMetadataRetriever();
                        metaRetriver.setDataSource(App.context ,  Uri.parse(audioFile.getFilePath()));
                    } catch (Exception e) {
                        // Log.e(TAG, "ex: " +e.getMessage() );
                    }


                    MediaMetadataRetriever finalMetaRetriver = metaRetriver;

                    String date = new JalaliCalendar().getJalaliDate(new Date(chat.getTime()));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(finalMetaRetriver != null) {
                                GlideManager.builder().loadBytes(finalMetaRetriver.getEmbeddedPicture() ,
                                        R.drawable.ic_audio , holder.img_audio_chat_row);

                                holder.txt_audio_artist_chat_row.setText(finalMetaRetriver
                                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                            }

                            holder.txt_audio_row_time_msg.setVisibility(View.VISIBLE);
                            holder.txt_audio_row_time_msg.setText(date);

                        }
                    });


                }
            });


        }else {
            GlideManager.builder().loadRes(R.drawable.ic_audio , holder.img_audio_chat_row);
        }


        holder.container_audio_play_row_chat.setBackgroundResource(chat.isSender() ?
                R.drawable.flat_red_box : R.drawable.flat_red_dark_box);
        holder.container_audio_row_chat.setGravity(chat.isSender() ?
                Gravity.END : Gravity.START);

    }


    private void handleApkMsg(ApkViewHolder holder, ChatValueModel chat) {

        FileModel apkFile = (FileModel) chat.getValue();

        long prg = apkFile.getProgress() ;

        if(prg == 100) {
           // holder.fill_loader_apk_chat_row.setVisibility(View.GONE);
            holder.prg_apk_chat_row.setVisibility(View.GONE);
            holder.img_apk_logo_chat_row.setVisibility(View.VISIBLE);
        }else {
            holder.prg_apk_chat_row.setProgress(apkFile.getProgress());
        }


        holder.container_apk_open_row_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prg == 100)
                    FileManager.builder().openApkFile(Uri.fromFile(new File(apkFile.getFilePath())));
            }
        });


        if(prg == 100) {

            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {

                    String date = new JalaliCalendar().getJalaliDate(new Date(chat.getTime()));
                    Drawable logo = ApkMsg.builder().getDrawable(apkFile.getFilePath());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            GlideManager.builder().loadDrawable(logo, holder.img_apk_logo_chat_row);

                            holder.txt_apk_row_time_msg.setVisibility(View.VISIBLE);
                            holder.txt_apk_row_time_msg.setText(date);

                        }
                    });

                }
            });

        }

        holder.txt_apk_name_chat_row.setText(apkFile.getFileName());
        holder.container_apk_open_row_chat.setBackgroundResource(chat.isSender() ?
                R.drawable.flat_red_box : R.drawable.flat_red_dark_box);
        holder.container_apk_row_chat.setGravity(chat.isSender() ?
                Gravity.END : Gravity.START);

    }


    private void handleVideoMsg(VideoViewHolder holder, ChatValueModel chat) {

        FileModel videoFile = (FileModel) chat.getValue();


        if(videoFile.getProgress() == 100) {

            holder.container_video_complete_row_chat.setVisibility(View.VISIBLE);
            holder.prg_video_play_row_chat.setVisibility(View.GONE);


            GlideManager.builder().loadVideoFrame(videoFile.getFilePath()  , R.drawable.ic_video ,
                    holder.img_video_complete_row_chat);


            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {
                    String date = new JalaliCalendar().getJalaliDate(new Date(chat.getTime()));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.txt_video_row_time_msg.setVisibility(View.VISIBLE);
                            holder.txt_video_row_time_msg.setText(date);
                        }
                    });

                }
            });


        } else {

            holder.container_video_complete_row_chat.setVisibility(View.GONE);
            holder.prg_video_play_row_chat.setVisibility(View.VISIBLE);

            if(chat.isSender())
                GlideManager.builder().loadVideoFrame(videoFile.getFilePath() , R.drawable.ic_video ,
                        holder.prg_video_play_row_chat);
            else

                GlideManager.builder().loadRes(R.drawable.ic_video ,
                        holder.prg_video_play_row_chat);

            holder.prg_video_play_row_chat.setValue(videoFile.getProgress());

        }


        holder.container_video_complete_row_chat.setGravity(chat.isSender() ? Gravity.END : Gravity.START);
        holder.container_video_row_chat.setGravity(chat.isSender() ? Gravity.END : Gravity.START);
        holder.img_video_complete_play_row_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.builder().openVideoFile(videoFile.getFilePath());
            }
        });

    }



    private void handleDocumentMsg(DocumentViewHolder holder, ChatValueModel chat) {
        FileModel docFile = (FileModel) chat.getValue();

        if(docFile.getProgress() == 100) {

            holder.progress_bar_document_file.setVisibility(View.GONE);

            GlideManager.builder().loadRes(FileManager.builder().getDocLogo(docFile.getFilePath()) ,
                    holder.img_document_row_time_msg , R.drawable.ic_document);


            App.getInstance().initThread(new OnThread() {
                @Override
                public void onReady() {
                    String date = new JalaliCalendar().getJalaliDate(new Date(chat.getTime()));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.txt_document_row_time_msg.setVisibility(View.VISIBLE);
                            holder.txt_document_row_time_msg.setText(date);
                        }
                    });

                }
            });


            holder.container_document_open_row_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileManager.builder().openDocFile( chat.isSender() ? Uri.parse(docFile.getFilePath())
                            : Uri.fromFile(new File(docFile.getFilePath())));
                }
            });


        } else {

            holder.progress_bar_document_file.setVisibility(View.VISIBLE);
            holder.progress_bar_document_file.setProgress(docFile.getProgress());

        }



        holder.container_document_row_chat.setGravity(chat.isSender() ? Gravity.END : Gravity.START);

        holder.container_document_open_row_chat.setBackgroundResource(chat.isSender() ?
                R.drawable.flat_red_box : R.drawable.flat_red_dark_box);

    }


    @Override
    public int getItemViewType(int position) {
        ChatValueModel chat = data.get(position);

        switch (chat.getMsgType()) {

            case Text:
                return  TxtViewType ;

            case Image:
                return  ImgViewType ;

            case Audio:
                return  AudioViewType ;

            case Video:
                return  VideoViewType ;

            case APK:
                return  ApkViewType ;

            case Document:
                return  DocumentViewType ;

        }

        return -1 ;
    }

    @Override
    public int getItemCount() {return data.size();}


    public class TxtViewHolder extends RecyclerView.ViewHolder {


        LinearLayout container_txt_row_chat ;
        EmojiTextView txt_row_chat ;
        TextView txt_row_time_msg ;


        public TxtViewHolder(@NonNull View itemView) {
            super(itemView);
            container_txt_row_chat = itemView.findViewById(R.id.container_txt_row_chat);
            txt_row_chat = itemView.findViewById(R.id.txt_row_chat);
            txt_row_time_msg = itemView.findViewById(R.id.txt_row_time_msg);
        }
    }



    public class ImgViewHolder extends RecyclerView.ViewHolder {


        LinearLayout container_img_row_chat ;
        RelativeLayout container_img_complete_row_chat;
        CircularMusicProgressBar prg_img_row_chat ;
        ImageView img_complete_row_chat ;
        TextView txt_img_row_time_msg ;


        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            container_img_row_chat = itemView.findViewById(R.id.container_img_row_chat);
            container_img_complete_row_chat = itemView.findViewById(R.id.container_img_complete_row_chat);
            prg_img_row_chat = itemView.findViewById(R.id.prg_img_row_chat);
            img_complete_row_chat = itemView.findViewById(R.id.img_complete_row_chat);
            txt_img_row_time_msg = itemView.findViewById(R.id.txt_img_row_time_msg);

        }
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {


        LinearLayout container_video_row_chat ;
        RelativeLayout container_video_complete_row_chat ;
        ImageView img_video_complete_row_chat ;
        ImageView img_video_complete_play_row_chat ;
        CircularMusicProgressBar prg_video_play_row_chat ;
        TextView txt_video_row_time_msg ;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            container_video_row_chat = itemView.findViewById(R.id.container_video_row_chat);
            container_video_complete_row_chat = itemView.findViewById(R.id.container_video_complete_row_chat);
            prg_video_play_row_chat = itemView.findViewById(R.id.prg_video_play_row_chat);
            img_video_complete_row_chat = itemView.findViewById(R.id.img_video_complete_row_chat);
            img_video_complete_play_row_chat = itemView.findViewById(R.id.img_video_complete_play_row_chat);
            txt_video_row_time_msg = itemView.findViewById(R.id.txt_video_row_time_msg);
        }
    }



    public class ApkViewHolder extends RecyclerView.ViewHolder {


        LinearLayout container_apk_row_chat , container_apk_open_row_chat ;
        ImageView img_apk_logo_chat_row ;
        ProgressBarIndicator prg_apk_chat_row;
        FillableLoader fill_loader_apk_chat_row ;
        TextView txt_apk_name_chat_row , txt_apk_row_time_msg ;


        public ApkViewHolder(@NonNull View itemView) {
            super(itemView);
            container_apk_row_chat = itemView.findViewById(R.id.container_apk_row_chat);
            container_apk_open_row_chat = itemView.findViewById(R.id.container_apk_open_row_chat);
            fill_loader_apk_chat_row = itemView.findViewById(R.id.fill_loader_apk_chat_row);
            prg_apk_chat_row = itemView.findViewById(R.id.prg_apk_chat_row);
            img_apk_logo_chat_row = itemView.findViewById(R.id.img_apk_logo_chat_row);
            txt_apk_name_chat_row = itemView.findViewById(R.id.txt_apk_name_chat_row);
            txt_apk_row_time_msg = itemView.findViewById(R.id.txt_apk_row_time_msg);
        }
    }



    public class AudioViewHolder extends RecyclerView.ViewHolder {


        LinearLayout container_audio_row_chat , container_audio_play_row_chat ;
        ProgressBarIndicator progress_bar_audio_file ;
        ImageView img_audio_chat_row ;
        TextView txt_audio_artist_chat_row , txt_audio_row_time_msg ;


        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            container_audio_row_chat = itemView.findViewById(R.id.container_audio_row_chat);
            container_audio_play_row_chat = itemView.findViewById(R.id.container_audio_play_row_chat);
            progress_bar_audio_file = itemView.findViewById(R.id.progress_bar_audio_file);
            img_audio_chat_row = itemView.findViewById(R.id.img_audio_chat_row);
            txt_audio_row_time_msg = itemView.findViewById(R.id.txt_audio_row_time_msg);
            txt_audio_artist_chat_row = itemView.findViewById(R.id.txt_audio_artist_chat_row);
        }
    }



    public class DocumentViewHolder extends RecyclerView.ViewHolder {


        LinearLayout container_document_row_chat , container_document_open_row_chat ;
        ProgressBarIndicator progress_bar_document_file ;
        TextView txt_document_row_time_msg ;
        ImageView img_document_row_time_msg;


        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            container_document_row_chat = itemView.findViewById(R.id.container_document_row_chat);
            container_document_open_row_chat = itemView.findViewById(R.id.container_document_open_row_chat);
            progress_bar_document_file = itemView.findViewById(R.id.progress_bar_document_file);
            txt_document_row_time_msg = itemView.findViewById(R.id.txt_document_row_time_msg);
            img_document_row_time_msg = itemView.findViewById(R.id.img_document_row_time_msg);
        }
    }

}

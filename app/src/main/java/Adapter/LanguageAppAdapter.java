package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Models.LanguageAppModel;
import activities.IntroActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.R;
;

/**
 * Created by soheilmohammadi on 8/3/18.
 */

public class LanguageAppAdapter extends RecyclerView.Adapter<LanguageAppAdapter.ViewHolder> {


    private static final String TAG = "LanguageAppAdapter";

    private ArrayList<LanguageAppModel> languageAppModels ;


    public LanguageAppAdapter(ArrayList<LanguageAppModel> languageAppModels) {
        this.languageAppModels = languageAppModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language_app ,
                parent , false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LanguageAppModel language = languageAppModels.get(position);
        setCurrentLocal(holder , language);
        setLanguageTxt(language , holder);
        setLanguageLogo(language , holder);
        setClickOnLanguage(language , holder);
    }


    private void setCurrentLocal(ViewHolder holder , LanguageAppModel languageAppModel) {

        if(App.getInstance().getCurrentLang().equals(languageAppModel.lang)) {
            holder.check_box_row_language_app.setChecked(true);
        }else {
            holder.check_box_row_language_app.setChecked(false);
        }

        holder.check_box_row_language_app.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b) {

                   notifyDataSetChanged();

                   if(!languageAppModel.lang.equals(App.getInstance().getCurrentLang())){
                       App.getInstance().setCurrentLang(languageAppModel.lang);
                   }
               }
            }
        });

    }

    private void setClickOnLanguage(LanguageAppModel language, ViewHolder holder) {
        holder.container_row_language_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().setCurrentLang(language.lang);
            }
        });
    }

    private void setLanguageLogo(LanguageAppModel language, ViewHolder holder) {
        Glide.with(App.context).load(language.logo).into(holder.img_logo_row_language_app);
    }

    private void setLanguageTxt(LanguageAppModel language, ViewHolder holder) {
        holder.txt_language_row_language_app.setText(language.langName);
    }

    @Override
    public int getItemCount() {
        return languageAppModels.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {

        LinearLayout container_row_language_app ;
        ImageView img_logo_row_language_app ;
        AppCompatCheckBox check_box_row_language_app ;
        TextView txt_language_row_language_app ;

        public ViewHolder(View itemView) {
            super(itemView);
            container_row_language_app = itemView.findViewById(R.id.container_row_language_app);
            img_logo_row_language_app = itemView.findViewById(R.id.img_logo_row_language_app);
            check_box_row_language_app = itemView.findViewById(R.id.check_box_row_language_app);
            txt_language_row_language_app = itemView.findViewById(R.id.txt_language_row_language_app);
        }
    }
}

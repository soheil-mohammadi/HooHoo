package Modules;



import android.content.Context;
import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import servers.monitor.fastest.hoohoonew.App;
import servers.monitor.fastest.hoohoonew.AppDatabase;

@Module
public class AppModule {

    public App app;
    public Context context ;

    public AppModule(App application , Context context) {
        this.app = application;
        this.context = context;
    }

    @Provides
    @Singleton
    App providesApplication() {
        return app;
    }


    @Provides
    @Singleton
    Retrofit providesRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getBaseUrl() )
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }




    @Provides
    @Singleton
    AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(context,
                AppDatabase.class, App.context.getPackageName()).allowMainThreadQueries().build() ;
    }

}
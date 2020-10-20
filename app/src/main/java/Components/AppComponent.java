package Components;

import javax.inject.Singleton;

import Modules.AppModule;
import dagger.Component;
import servers.monitor.fastest.hoohoonew.App;

@Singleton
@Component(modules={AppModule.class})

public interface AppComponent {

   void inject(App application);
   App app();

}
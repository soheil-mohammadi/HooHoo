package ViewModels;

import java.util.ArrayList;

import Models.AppModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectedAppsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<AppModel>> selectedApps  = new MutableLiveData<>();

    public void setSelectedApps(ArrayList<AppModel> apps) {
        selectedApps.setValue(apps);
    }


    public LiveData<ArrayList<AppModel>> onSelectedApps() {
        return  selectedApps;
    }

    public void clearAllData() {
        selectedApps.setValue(new ArrayList<>());
    }
}

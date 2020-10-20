package Listeners;

import Models.AppModel;

public interface OnSelectionApps {

    void onSelected(AppModel appModel);
    void onDeselected(AppModel appModel);
}

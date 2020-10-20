package Listeners;

public interface OnSeachCahngedToolbar {


    void onOpened();

    void onChanged(CharSequence query);

    void onSearchBtnClicked(CharSequence query);

    void onClose();
}

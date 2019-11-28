package cmput301.moodi.ui.loggedIn.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Class: ProfileViewModel
 * A viewModel for user's profile
 */

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
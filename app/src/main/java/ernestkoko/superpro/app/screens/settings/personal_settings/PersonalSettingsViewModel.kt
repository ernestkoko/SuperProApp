package ernestkoko.superpro.app.screens.settings.personal_settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalSettingsViewModel : ViewModel() {

    var personalName = MutableLiveData<String>()
        set(value) {
            personalName.value = value.value
        }
    var phoneNumber = MutableLiveData<String>()
    set(value) {
        phoneNumber.value = value.value
    }
}
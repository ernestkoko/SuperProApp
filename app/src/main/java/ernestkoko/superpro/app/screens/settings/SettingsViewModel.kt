package ernestkoko.superpro.app.screens.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _isPersonalSettingsClicked = MutableLiveData<Boolean>()
    val isPersonalSettingsClicked:LiveData<Boolean>
    get() = _isPersonalSettingsClicked

    //called when personal settings text view is clicked
    fun onPersonalSettingsClick(){
        _isPersonalSettingsClicked.value = true
    }
    //when done with navigating to personal settings
    fun donNavigatingToPersonalSettings(){
        _isPersonalSettingsClicked.value = false
    }
}
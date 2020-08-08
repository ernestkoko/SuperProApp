package ernestkoko.superpro.app.screens.settings.personal_settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ernestkoko.superpro.app.firebase.PersonalDetails

class PersonalSettingsViewModel : ViewModel() {
    private val TAG = "PSettingVModel"
    val mDatabase = Firebase.database.reference

    var personalName = MutableLiveData<String>()
        set(value) {
            personalName.value = value.value
        }
    var phoneNumber = MutableLiveData<String>()
        set(value) {
            phoneNumber.value = value.value
        }
    //observable fields
    private val _isImageViewClicked = MutableLiveData<Boolean>()
    val isImageViewClicked: LiveData<Boolean>
    get() = _isImageViewClicked

    fun addPersonDetails() {
        if (!personalName.value.toString().isNullOrEmpty() && !phoneNumber.value.toString()
                .isNullOrEmpty()
        ) {
            Log.i(TAG,"Fields: Not null")
            val personDetails = PersonalDetails(personalName.value?.trim(),phoneNumber.value?.trim())
            //fields are not null
            mDatabase.child("product").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("personal_details").push().setValue(personDetails)

        } else {
            //fields are empty
            Log.i(TAG,"Fields:  null")
        }
    }

    //fun called when personal image view is clicked
    fun personalImageClicked(){
        Log.i(TAG,"Image:Clicked")
        //set the value to true
        _isImageViewClicked.value = true

    }
    //done clicking Image view
    fun doneClickingImageView(){
        _isImageViewClicked.value = false
    }
}
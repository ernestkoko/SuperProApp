package ernestkoko.superpro.app.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {
    private val TAG = "RegViewModel"
    private val auth = FirebaseAuth.getInstance()

    //user email
    var email = MutableLiveData<String>()
        set(value) {
            email.value = value.value
        }


    //user password
    var password1 = MutableLiveData<String>()
        set(value) {
            password1.value = value.value
        }


    //user password2
    var password2 = MutableLiveData<String>()
        set(value) {
            password2.value = value.value
        }

    //observable fields
    private val _areFieldsEmpty = MutableLiveData<Boolean>()
    val areFieldsEmpty: LiveData<Boolean>
        get() = _areFieldsEmpty
    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean>
        get() = _isEmailValid
    private val _doPasswordsMatch = MutableLiveData<Boolean>()
    val doPasswordsMatch: LiveData<Boolean>
        get() = _doPasswordsMatch
    private val _wasUserRegistered = MutableLiveData<Boolean>()
    val wasUserRegistered: LiveData<Boolean>
        get() = _wasUserRegistered
    private val _wasEmailSent = MutableLiveData<Boolean>()
    val wasEmailSent: LiveData<Boolean>
        get() = _wasEmailSent
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean>
        get() = _showDialog


    //function that registers new user
    fun registerUser() {
        Log.i(TAG, "registerUser: Called")
        if (!email.value?.trim().isNullOrEmpty() && !password1.value?.trim().isNullOrEmpty()
            && !password2.value?.trim().isNullOrEmpty()
        ) {
            _areFieldsEmpty.value = false

            Log.i(
                TAG, "Fields are not empty: email is  ${email.value}," +
                        " Password1: ${password1.value}, Password2 ${password2.value}"
            )
            //inputs are not null or empty
            //check if email is valid
            if (isValidEmail(email.value!!.trim())) {
                Log.i(TAG, "Email is valid")
                //email is valid
                _isEmailValid.value = true
                //check if passwords match
                if (doStringsMatch(password1.value.toString(), password2.value.toString())) {
                    Log.i(TAG, "Passwords match")
                    //register the user with the mail and password
                    _doPasswordsMatch.value = true
                    //show dialog
                    _showDialog.value = true
                    auth.createUserWithEmailAndPassword(
                        email.value!!.trim(),
                        password1.value!!.trim()
                    ).addOnCompleteListener { task ->
                        //check if task is successful
                        if (task.isSuccessful) {
                            Log.i(TAG, "User Reg: Successful")
                            //send verification email to the new user
                            sendVerificationEmail()
                            _wasUserRegistered.value = true
                            //hide dialog
                            _showDialog.value = false

                        } else {
                            Log.i(TAG, "User Reg: failed")
                            Log.i(TAG, task.exception!!.message)
                            //notify the user the reg failed
                            _wasUserRegistered.value = false
                            //hide dialog
                            _showDialog.value = false
                        }
                    }
                } else {
                    Log.i(TAG, "Passwords do not match")
                    //passwords do not match
                    _doPasswordsMatch.value = false
                }
            } else {
                Log.i(TAG, "Email: not valid")
                _isEmailValid.value = false

            }
        } else {
            Log.i(TAG, "Inputs are null or empty")
            // inputs are null or empty
            _areFieldsEmpty.value = true
        }

    }

    private fun sendVerificationEmail() {
        Log.i(TAG, "sendVerificationEmail: called")
        auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            //check if email was sent
            if (task.isSuccessful) {
                Log.i(TAG, "Verification Mail: Sent")
                //notify the user the email was sent and log him out
                _wasEmailSent.value = true
                auth.signOut()
            } else {
                //else notify the user the email was not sent
                Log.i(TAG, " Verification Mail: not sent")
                Log.i(TAG, task.exception!!.message)
                _wasEmailSent.value = false
            }
        }

    }

    /**
     * @param s1 is the first string
     * @param s2 is the second string
     * returns true if the both strings are equal or match
     */
    fun doStringsMatch(s1: String, s2: String): Boolean {
        //returns true if the two passwords are equal

        return s1 == s2

    }

    /**
     * @param email is the email to validate
     * returns true if the email meets required email standard
     */
    private fun isValidEmail(email: CharSequence): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        //returns true if email is not empty and it matches standard ema?il format
        return email.matches(emailPattern.toRegex())

    }
}
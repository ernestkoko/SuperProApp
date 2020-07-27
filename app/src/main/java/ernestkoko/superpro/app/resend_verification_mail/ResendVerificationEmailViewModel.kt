package ernestkoko.superpro.app.resend_verification_mail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResendVerificationEmailViewModel : ViewModel() {
    private val TAG = "ResendVeriModel"
    private val mAuth = FirebaseAuth.getInstance()
    var email = MutableLiveData<String>()
        set(value) {
            email.value = value.value
        }
    var password = MutableLiveData<String>()
        set(value) {
            password.value = value.value
        }
    private val _areFieldsEmpty = MutableLiveData<Boolean>()
    val areFieldsEmpty: LiveData<Boolean>
        get() = _areFieldsEmpty
    private val _isEmailSent = MutableLiveData<Boolean>()
    val isEmailSent: LiveData<Boolean>
        get() = _isEmailSent
    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean>
        get() = _isUserLoggedIn
    private val _isValidEmail = MutableLiveData<Boolean>()
    val isValidEmail: LiveData<Boolean>
        get() = _isValidEmail


    //fun for resend button
    fun resendEmail() {
        Log.i(TAG, "ResendEmail: Called")
        //check if fields are empty
        if (!email.value?.trim().isNullOrEmpty() && !password.value?.trim().isNullOrEmpty()) {
            //fields are not empty
            _areFieldsEmpty.value = false
            if (isValidEmail(email.value!!.trim())) {
                //email is valid
                _isValidEmail.value = true
                mAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i(TAG, "Signed in")
                            //logged in
                            _isUserLoggedIn.value = true
                            //send the email
                            sendVerificationMail()
                        } else {
                            Log.i(TAG, "Could not sign in")
                            //log in not successful
                            _isUserLoggedIn.value = false
                        }
                    }
            } else {
                //email is not valid
                _isValidEmail.value = false
            }


        } else {
            //fields are empty
            _areFieldsEmpty.value = true
        }

    }

    private fun sendVerificationMail() {
        Log.i(TAG, "sendVerificationMail: Called")
        val user = mAuth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "Email: Sent")
                //email sent
                _isEmailSent.value = true
            } else {
                //email not send
                Log.i(TAG, "Email: Not Sent")
                _isEmailSent.value = false
            }
        }
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
package ernestkoko.superpro.app.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val TAG = "LoginViewModel"
    private val auth = FirebaseAuth.getInstance()
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private var mUser = auth.currentUser

    //user email
    var email = MutableLiveData<String>()
        set(value) {
            email.value = value.value
        }


    //user password
    var password = MutableLiveData<String>()
        set(value) {
            password.value = value.value
        }

    //observable field
    private val _areFieldsEmpty = MutableLiveData<Boolean>()
    val areFieldsEmpty: LiveData<Boolean>
        get() = _areFieldsEmpty
    private val _isEmailVerified = MutableLiveData<Boolean>()
    val isEmailVerified: LiveData<Boolean>
        get() = _isEmailVerified
    private val _isUserSignedIn = MutableLiveData<Boolean>()
    val isUserSignedIn: LiveData<Boolean>
        get() = _isUserSignedIn
    private val _isCheckEmailAndPassword = MutableLiveData<Boolean>()
    val isCheckEmailAndPassword: LiveData<Boolean>
    get() = _isCheckEmailAndPassword
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean>
    get() =  _showDialog
    private val _isLoginButtonClicked = MutableLiveData<Boolean>()
    val isLoginButtonClicked: LiveData<Boolean>
    get() = _isLoginButtonClicked
    private val _isResendVerificationMailClicked = MutableLiveData<Boolean>()
    val isResendVerificationMailClicked: LiveData<Boolean>
    get() = _isResendVerificationMailClicked
    private val _isResetPasswordClicked = MutableLiveData<Boolean>()
    val isResetPasswordClicked:LiveData<Boolean>
    get() = _isResetPasswordClicked



    init {
        //setup the auth state listener
        setUpAuthStateListener()
        //add the auth state listener on init
        auth.addAuthStateListener(mAuthStateListener)

    }

    private fun setUpAuthStateListener() {

        Log.d(TAG, "setupFirebaseAuth: started.");
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->

            //check if user is not null
            if (mUser != null) {
                Log.i(TAG, "User: Not null")
                Log.i(TAG, mUser!!.email.toString())
                //check if user is verified
                if (mUser!!.isEmailVerified) {
                    Log.i(TAG, "User: Email verified")
                    //email is verified
                    _isEmailVerified.value = true
                } else {
                    Log.i(TAG, "User: Email not verified")
                    //email is not verified
                    _isEmailVerified.value = false
                }

            } else {
                Log.i(TAG, " User is null")
            }

        }
    }

    //method for login in user
    fun loginUser() {
        Log.i(TAG, "loginUser: called")
        _isLoginButtonClicked.value = true
        //check if the fields are empty
        if (!password.value?.trim().isNullOrEmpty() && !email.value?.trim().isNullOrEmpty()) {
            //fields are not empty
            _areFieldsEmpty.value = false
            _showDialog.value = true
            //check if the email has been verified
            auth.signInWithEmailAndPassword(email.value!!.trim(), password.value!!.trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(TAG, "User: Signed in")
                        //check the auth state of the user
                        setUpAuthStateListener()
                        //check if the email is verified
                        if (mUser!!.isEmailVerified) {
                            //sign in successful
                            //sign in the user
                            _isUserSignedIn.value = true
                            _showDialog.value = false
                        }else{
                            //user can not go to home page
                            _isUserSignedIn.value = false
                            _showDialog.value = false
                        }
                        //  _isUserSignedIn.value = true
                    } else {
                        Log.i(TAG, "User: Not SignedIn")
                        Log.i(TAG, task.exception!!.message.toString())
                        //sign in failed
                        _isUserSignedIn.value = false
                        _isCheckEmailAndPassword.value = true
                        _showDialog.value = false
                    }

                }


        } else {
            //fields are null or empty
            _areFieldsEmpty.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        //check if the state listener is null before removing
        if (mAuthStateListener != null) {
            auth.removeAuthStateListener(mAuthStateListener)
        }


    }
    fun resendVerificationMail(){
        _isResendVerificationMailClicked.value = true
    }
    fun doneNavigatingToResendEmail(){
        _isResendVerificationMailClicked.value = false
    }

    //fun called when reset password is pressed
    fun resetPassword(){
        Log.i(TAG,"ResetPassword: Called")
        //set the value to true
        _isResetPasswordClicked.value = true
    }
    fun doneClickingResetPassword(){
        //set the value to false
        _isResetPasswordClicked.value = false
    }
}
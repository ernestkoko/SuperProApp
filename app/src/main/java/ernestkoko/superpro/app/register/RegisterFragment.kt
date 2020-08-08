package ernestkoko.superpro.app.register

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.RegisterFragmentBinding
import ernestkoko.superpro.app.utils.KeyBoard

class RegisterFragment : Fragment() {
    private val TAG = "RegViewModel"
    private lateinit var mBinding: RegisterFragmentBinding
    private lateinit var mUserEmail: EditText
    private lateinit var mUserPassword: EditText
    private lateinit var mRegButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDialog: Dialog

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        //nav controller
        val navController = findNavController()
      //  val appBarConfig = AppBarConfiguration(navController.graph)
        mBinding.registrationToolBar.setupWithNavController(navController)
        //initialise the auth
        mAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        mBinding.regViewModel = viewModel
        mBinding.setLifecycleOwner(this)
//listen for when inputs are empty
        viewModel.areFieldsEmpty.observe(viewLifecycleOwner, Observer { areFieldsEmpty ->
            if (areFieldsEmpty) {
                //toast a message to the user
                Toast.makeText(requireContext(), "No field should be empty", Toast.LENGTH_LONG)
                    .show()
            }
        })
        //listen for when email is not valid
        viewModel.isEmailValid.observe(viewLifecycleOwner, Observer { isEmailValid ->
            if (!isEmailValid) {
                //toast a message to the user
                Toast.makeText(requireContext(), "Email is not valid", Toast.LENGTH_LONG)
                    .show()
            }
        })
        //listen for when passwords do not match
        viewModel.doPasswordsMatch.observe(viewLifecycleOwner, Observer { doPasswordsMatch ->
            if (!doPasswordsMatch) {
                //toast a message to the user
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_LONG)
                    .show()
            }
        })
        //listen for when user is registered
        viewModel.wasUserRegistered.observe(viewLifecycleOwner, Observer { userRegistered ->
            if (userRegistered) {
                //toast a message to the user
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_LONG)
                    .show()
            } else {
                //toast a message to the user
                Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_LONG)
                    .show()
            }

        })
        //listen for when email is sent
        viewModel.wasEmailSent.observe(viewLifecycleOwner, Observer { wasEmailSent ->
            if (wasEmailSent) {
                //toast a message to the user
                Toast.makeText(requireContext(), "Verification Email: Sent", Toast.LENGTH_LONG)
                    .show()
                //navigate the user to the login screen
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                //toast a message to the user
                Toast.makeText(
                    requireContext(),
                    "Verification Email: Failed to send",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
        //listen for when to show dialog
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { showDialog ->
            if (showDialog) {
                showProgressDialog()
            } else {
                hideDialog()
            }
        })
        //listen for when reg new user button is clicked
        viewModel.isRegisterUserButtonClicked.observe(viewLifecycleOwner, Observer { isRegisterUserButtonCllicked ->
            if (isRegisterUserButtonCllicked){
                KeyBoard.hidKeyBoard(this.requireActivity())
            }
        })

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//        mRegButton.setOnClickListener { view ->
//            Log.i(TAG,"RegButton: Clicked")
//            createNewUser()
//        }

    }

    private fun createNewUser() {
        Log.i(TAG, "CreateNewUser: called")
        mAuth.createUserWithEmailAndPassword(
            mUserEmail.text.toString(),
            mUserPassword.text.toString()
        )
            .addOnCompleteListener { auth ->
                if (auth.isSuccessful) {
                    Log.i(TAG, "User creation: Successful")
                    sendVerificationMail(mAuth.currentUser!!)
                } else {
                    Log.i(TAG, "User creation: Failed " + auth.exception!!.message)

                }

            }


    }

    private fun sendVerificationMail(user: FirebaseUser) {
        Log.i(TAG, "sendVerificationMail: Called")
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "Verification Mail: sent")
                mAuth.signOut()
            } else {
                Log.i(TAG, "Verification Mail: Not sent")
            }
        }
    }

    private fun showProgressDialog() {
        mDialog = Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar)
        mDialog.setContentView(R.layout.my_progress_bar)
        mDialog.setCancelable(false)
        mDialog.show()

    }

    private fun hideDialog() {
        mDialog.dismiss()

    }

}
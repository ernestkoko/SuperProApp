package ernestkoko.superpro.app.resend_verification_mail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.ResendVerificationEmailFragmentBinding

class ResendVerificationEmailFragment : Fragment() {
    private lateinit var mBinding: ResendVerificationEmailFragmentBinding
    private lateinit var mDialog: Dialog

    companion object {
        fun newInstance() =
            ResendVerificationEmailFragment()
    }

    private lateinit var viewModel: ResendVerificationEmailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.resend_verification_email_fragment,
            container,
            false
        )
        //nav controller
        val navController = findNavController()
        mBinding.resendVerificationToolBar.setupWithNavController(navController)
        viewModel = ViewModelProvider(this).get(ResendVerificationEmailViewModel::class.java)
        //sets the view model
        mBinding.resendEmailViewModel = viewModel

        //listen for when fields are empty
        viewModel.areFieldsEmpty.observe(viewLifecycleOwner, Observer { areFieldsEmpty ->
            if (areFieldsEmpty) {
                //fields are empty
                Toast.makeText(requireContext(), "Fields are empty!", Toast.LENGTH_LONG).show()
            }
        })
        //listen for when email or password is not ok
        viewModel.isValidEmail.observe(viewLifecycleOwner, Observer { isEmailValid ->
            if (!isEmailValid) {
                //set error message to the user
                mBinding.resendEmailLayout.error = "Email is not valid!"

            }
        })
        //listen for when user is logged in
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer { isUserLoggedin ->
            if (!isUserLoggedin) {
                //could not log in user
                Toast.makeText(requireContext(), "Could not find the account", Toast.LENGTH_LONG)
                    .show()
                //set error message to the user
                mBinding.resendEmailLayout.error = "Check the email and password"
            }
        })
        //listen for when email is sent
        viewModel.isEmailSent.observe(viewLifecycleOwner, Observer { isEmailSent ->
            if (isEmailSent) {
                //email was sent
                Toast.makeText(requireContext(), "Verification Email Resent!", Toast.LENGTH_LONG)
                    .show()
                //navigate to log in fragment
                findNavController().navigate(R.id.action_resendVerificationEmailFragment_to_loginFragment)
            } else {
                Toast.makeText(requireContext(), "Verification Not Sent!", Toast.LENGTH_LONG).show()
            }
        })
        //listen for when resend email button is clicked
        viewModel.isResendEmailButtonClicked.observe(
            viewLifecycleOwner,
            Observer { isButtonclicked ->
                if (isButtonclicked) {
                    //set the errors to null
                    mBinding.resendEmailLayout.error = null
                }
            })
        //listen for when to show dialog
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { showDialog->
            if (showDialog){
                //show dialog
                showProgressDialog()
            }else{
                //hide the progress dialog
                hideDialog()
            }
        })
        return mBinding.root
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
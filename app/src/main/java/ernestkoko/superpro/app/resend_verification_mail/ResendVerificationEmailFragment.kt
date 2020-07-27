package ernestkoko.superpro.app.resend_verification_mail

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
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.ResendVerificationEmailFragmentBinding

class ResendVerificationEmailFragment : Fragment() {
    private lateinit var mBinding: ResendVerificationEmailFragmentBinding

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
                //email is not valid
                Toast.makeText(requireContext(), "Email is not valid!", Toast.LENGTH_LONG).show()
            }
        })
        //listen for when user is logged in
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer { isUserLoggedin ->
            if (!isUserLoggedin) {
                //could not log in user
                Toast.makeText(requireContext(), "Could not find the account", Toast.LENGTH_LONG)
                    .show()
                //set error message to the user
               mBinding.resendEmailLayout.error ="Check the email and password"
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
        return mBinding.root
    }


}
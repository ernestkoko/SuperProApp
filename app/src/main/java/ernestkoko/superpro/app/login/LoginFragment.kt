package ernestkoko.superpro.app.login


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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration


import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.LoginFragmentBinding

import ernestkoko.superpro.app.utils.HideKeyBoard

class LoginFragment : Fragment() {
    private val TAG = "LogInFrag"
    private lateinit var mBinding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var mDialog: Dialog

    companion object {
        fun newInstance() = LoginFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)


        // nav controller
       val navController = findNavController()
      //  val appBarConfiguration = AppBarConfiguration(navController.graph)
//       mBinding.loginToolBar.setupWithNavController(navController, appBarConfiguration)
        //initialise view model
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //set the viewModel to the binding class
        mBinding.loginViewModel = viewModel
        mBinding.setLifecycleOwner(this)


        // set on click listener to the reg button
        mBinding.loginRegButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //listen to the observable fields for the necessary actions
        viewModel.areFieldsEmpty.observe(viewLifecycleOwner, Observer { areFieldsEmpty ->
            if (areFieldsEmpty) {
                Toast.makeText(requireContext(), "Fields are empty", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.isEmailVerified.observe(viewLifecycleOwner, Observer { isEmailVerified ->
            if (!isEmailVerified) {
                Toast.makeText(
                    requireContext(),
                    "Email is not verified\nCheck your inbox",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        viewModel.isUserSignedIn.observe(viewLifecycleOwner, Observer { isUserSignedIn ->
            if (isUserSignedIn) {
                //navigate to the home page
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })
        //check if password or email is wrong
        viewModel.isCheckEmailAndPassword.observe(
            viewLifecycleOwner,
            Observer { isCheckEmailAndPassword ->
                if (isCheckEmailAndPassword) {
                    Toast.makeText(
                        requireContext(),
                        "Check the email and password",
                        Toast.LENGTH_LONG
                    ).show()
                    mBinding.emailLayout.error = "Check the email"
                    mBinding.passwordLayout.error = "Check password"
                }
            })

        //check to show dialog
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { showDialog ->
            if (showDialog) {
                //show dialog
                showProgressDialog()
            } else {
                //hide the dialog
                hideDialog()
            }
        })

        //listen for when the login button is clicked
        viewModel.isLoginButtonClicked.observe(viewLifecycleOwner, Observer { isLoginButtonClicked ->
            if (isLoginButtonClicked){
               HideKeyBoard.hidKeyBoard(this.requireActivity())
            }

        })

        //listen for when resend verification mail is clicked
        viewModel.isResendVerificationMailClicked.observe(viewLifecycleOwner, Observer { isResendEmailClicked ->
            if (isResendEmailClicked){
                //navigate to resend verification mail fragment
                findNavController().navigate(R.id.action_loginFragment_to_resendVerificationEmailFragment)
                //set the live data to false
                viewModel.doneNavigatingToResendEmail()
            }
        })


        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

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
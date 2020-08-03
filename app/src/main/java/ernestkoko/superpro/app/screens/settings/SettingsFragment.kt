package ernestkoko.superpro.app.screens.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {
    private lateinit var mBinding:SettingsFragmentBinding

    companion object {
        fun newInstance() =
            SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.settings_fragment, container, false)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        //get the nav controller
        val navController = findNavController()
        //set up the up left button of the tool bar
        mBinding.settingsToolBar.setupWithNavController(navController)
        //connect the viewModel
        mBinding.settingsViewModel = viewModel

        //observe when personal settings textView is clicked
        viewModel.isPersonalSettingsClicked.observe(viewLifecycleOwner, Observer { isPersonalSettingsClicked ->
            if (isPersonalSettingsClicked){
                //navigate to personal settings fragment
                navController.navigate(R.id.action_settingsFragment_to_personalSettingsFragment)
                //set the value of the live data to false
                viewModel.donNavigatingToPersonalSettings()
            }
        })




        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}
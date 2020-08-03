package ernestkoko.superpro.app.screens.settings.personal_settings

import android.database.DatabaseUtils
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.PersonalSettingsFragmentBinding

class PersonalSettingsFragment : Fragment() {
    private lateinit var mBinding: PersonalSettingsFragmentBinding

    companion object {
        fun newInstance() =
            PersonalSettingsFragment()
    }

    private lateinit var viewModel: PersonalSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.personal_settings_fragment, container, false)
        //find the nav controller
        val navController = findNavController()
        //set up the upper left arrow
        mBinding.personalSettingToolBar.setupWithNavController(navController)
       mBinding.personalSettingToolBar.inflateMenu(R.menu.personal_settings_menu)

       //initialise the view model
        viewModel = ViewModelProvider(this).get(PersonalSettingsViewModel::class.java)
        //connect the view model
        mBinding.personalSettingsViewModel = viewModel

        //set hasOptionsMenu to true
        setHasOptionsMenu(true)
        return mBinding.root

    }
    

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}
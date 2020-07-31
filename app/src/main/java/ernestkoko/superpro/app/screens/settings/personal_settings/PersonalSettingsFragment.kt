package ernestkoko.superpro.app.screens.settings.personal_settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ernestkoko.superpro.app.R

class PersonalSettingsFragment : Fragment() {

    companion object {
        fun newInstance() =
            PersonalSettingsFragment()
    }

    private lateinit var viewModel: PersonalSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.personal_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PersonalSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
package ernestkoko.superpro.app.screens.settings.personal_settings

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.DatabaseUtils
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.PersonalSettingsFragmentBinding

class PersonalSettingsFragment : Fragment() {
    private lateinit var mBinding: PersonalSettingsFragmentBinding
    private val TAG = "PersonalSetFrag"
    private val CAMERA_REQUEST_CODE = 1000

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
        //observe when image view is clicked
        viewModel.isImageViewClicked.observe(viewLifecycleOwner, Observer { isImageClicked ->
            if (isImageClicked){
                Log.i(TAG,"ImageView:Clicked")
                //get a dialog
                popUpDialog()
                //set the observable field to false
                viewModel.doneClickingImageView()

            }
        })

        //set hasOptionsMenu to true
        setHasOptionsMenu(true)
        return mBinding.root

    }

    //fun that pops the alert dialog
    private fun popUpDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Personal Picture")
            .setPositiveButton("Camera", { dialogInterface: DialogInterface, i: Int ->
                Log.i(TAG, "onClick: Taking photo")
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }).setNegativeButton("Memory", { dialogInt: DialogInterface, i: Int ->
                Log.i(TAG, "Memory: Clicked")
            }).show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
     when(item.itemId){
        // R.id.save_personal_details

     }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE){
            Log.i(TAG,"Data: from camera")
            if (resultCode == Activity.RESULT_OK){
                Log.i(TAG, data!!.extras.toString())
                Log.i(TAG,"Camera: result is ok")
            }
        }
    }

}
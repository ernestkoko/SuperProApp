package ernestkoko.superpro.app.screens.settings.personal_settings

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.squareup.picasso.Picasso
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.PersonalSettingsFragmentBinding
import java.io.ByteArrayOutputStream
import java.io.File

class PersonalSettingsFragment : Fragment() {
    private lateinit var mBinding: PersonalSettingsFragmentBinding
    private val TAG = "PersonalSetFrag"
    private val CAMERA_REQUEST_CODE = 1000
    private val PICK_FILE_REQUEST_CODE = 1001
    private var mStoragePermissions: Boolean = false
    private val PERM_REQUEST_CODE = 200

    companion object {
        fun newInstance() =
            PersonalSettingsFragment()
    }

    private lateinit var viewModel: PersonalSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.personal_settings_fragment, container, false)
        //verify permission
        verifyStoragePermission()
        //find the nav controller
        val navController = findNavController()
       //get the tool bar from xml
        val toolBar = mBinding.personalSettingToolBar
        //set up the upper left arrow
        toolBar.setupWithNavController(navController)
       //get the menu from the tool bar
        val me = toolBar.menu
        //iterate through the menu to pick each item
        me.forEach {
            Log.i(TAG, it.itemId.toString())
            if (it.itemId == R.id.save_personal_details){
                Log.i(TAG, "Save picked")
                it.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener{
                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
                        when(p0!!.itemId){
                            R.id.save_personal_details ->  Log.i(TAG,"Save Clicked")
                            else -> return true

                        }
                        return true
                    }
                })
            }
        }
        Log.i(TAG, me.size.toString())
//       val item = me.
//        item.title = "Savy"

        //initialise the view model
        viewModel = ViewModelProvider(this).get(PersonalSettingsViewModel::class.java)
        //connect the view model
        mBinding.personalSettingsViewModel = viewModel
        //observe when image view is clicked
        viewModel.isImageViewClicked.observe(viewLifecycleOwner, Observer { isImageClicked ->
            if (isImageClicked) {
                if (mStoragePermissions) {
                    Log.i(TAG, "ImageView:Clicked")
                    //get a dialog
                    popUpDialog()
                    //set the observable field to false
                    viewModel.doneClickingImageView()
                } else {
                    //verify permission
                    verifyStoragePermission()
                    viewModel.doneClickingImageView()
                }

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
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
            }).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "Data: from camera")
            val camBitmap = data!!.extras!!.get("data") as Bitmap
            val imageUri = convertBitmapToUri(camBitmap)
            Log.i(TAG, camBitmap.toString())
            Picasso.get().load(imageUri).placeholder(R.drawable.ic_human_pic)
                .error(R.drawable.ic_human_pic).into(mBinding.personalPicImageView)

        }else if (requestCode == PICK_FILE_REQUEST_CODE && resultCode ==Activity.RESULT_OK){
            Log.i(TAG, "Mem: Result ok")
            val imageUri = data?.data
            Log.i(TAG, imageUri.toString())
            Picasso.get().load(imageUri).placeholder(R.drawable.ic_human_pic)
                .error(R.drawable.ic_human_pic).into(mBinding.personalPicImageView)
        }
    }
    /**
     * converts bitmap to uri and return the uri
     * @param bitmap is the input bitmap
     */
    private fun convertBitmapToUri(bitmap: Bitmap): Uri {
        val file = File(requireContext().cacheDir, "ImageFile")
        file.delete() // delete in case it exists
        file.createNewFile()
        val outputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        outputStream.write(byteArray)
        outputStream.flush()
        outputStream.close()
        byteArrayOutputStream.close()
        val uri = file.toURI().toString()
        //return the uri
        return Uri.parse(uri)

    }

    /**
     * method for verifying if the user has granted the required permissions
     */
    private fun verifyStoragePermission() {
        Log.i("NewProdFrag", "verifyStoragePermission called")
        //arrays of Strings of permission required
        val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        )
        // check if the Phone's SDK is equal to or greater than version M
        //because from M and greater requires user to grant permission when it is needed and not
        //the app is being installed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check if permissions have been granted
            if (requireContext().checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED
                && requireContext().checkSelfPermission(permissions[1]) == PackageManager.PERMISSION_GRANTED
                && requireContext().checkSelfPermission(permissions[2]) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "Permissions granted: Setting mStoragePermissions to true")
                //if granted
                mStoragePermissions = true

            } else {
                Log.i("NewProdFrag", "VerifyPermissions: Asking user for permission")
                //if not granted, request for permissions
                ActivityCompat.requestPermissions(requireActivity(), permissions, PERM_REQUEST_CODE)

            }
        }

    }

}
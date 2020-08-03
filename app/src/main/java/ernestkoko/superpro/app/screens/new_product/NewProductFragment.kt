package ernestkoko.superpro.app.screens.new_product

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.NewProductFragmentBinding
import ernestkoko.superpro.app.dialog.CustomProgressDialog
import ernestkoko.superpro.app.utils.HideKeyBoard
import java.io.ByteArrayOutputStream
import java.io.File

class NewProductFragment : Fragment(), ChangePhotoDialog.OnPhotoReceivedListener {
    private val TAG = "NewProdFrag"
    private lateinit var mBinding: NewProductFragmentBinding
    private var mSelectedImageBitMap: Bitmap? = null
    private var mSelectedImageUri: Uri? = null
    private var mStoragePermissions: Boolean = false
    private val PERM_REQUEST_CODE = 200
    private val SAVE_IMAGE_REQUEST_CODE: Int = 2001
    private val PICK_IMAGE_REQUEST_CODE: Int = 2000
    private val mCustomDialog = CustomProgressDialog()

    // private val mProgressDialog = ProgressDialogCustom()
    private lateinit var mDialog: Dialog


    companion object {
        fun newInstance() =
            NewProductFragment()
    }

    private lateinit var viewModel: NewProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.new_product_fragment, container, false)

        //find nav controller
        val navController = findNavController()
        mBinding.newProductToolBar.setupWithNavController(navController)
        viewModel = ViewModelProvider(this).get(NewProductViewModel::class.java)
        mBinding.newProductViewModel = viewModel
        //verify storage permission
        verifyStoragePermission()

        //picking image
        viewModel.isImageClicked.observe(viewLifecycleOwner, Observer { isImageViewClicked ->
            if (isImageViewClicked) {
                if (mStoragePermissions) {
                    val dialog = ChangePhotoDialog()
                    dialog.setTargetFragment(this, 300)
                    dialog.show(parentFragmentManager, "ChangePhotoDialog")
                    //
                    viewModel.finishWithPicture()
                } else {
                    verifyStoragePermission()
                    viewModel.finishWithPicture()
                }
            }

        })
//        mBinding.newProductImage.setOnClickListener {
//            if (mStoragePermissions) {
//                val dialog = ChangePhotoDialog()
//                dialog.setTargetFragment(this, 300)
//                dialog.show(parentFragmentManager, "ChangePhotoDialog")
//            } else {
//                verifyStoragePermission()
//            }
//        }
        //observe when to show progress dialog
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { showDialog ->
            if (showDialog) {
                Log.i(TAG, "Showing Dialog")
                //mProgressDialog.show(parentFragmentManager,"CustomDialog")
                showProgressDialog()


//                mCustomDialog.show(requireContext(),"Uploading, Please wait...")
//                mCustomDialog.show(requireContext())
            } else {
                //  mProgressDialog.dismiss()
                Log.i(TAG, "Dismissing Dialog")
                // mCustomDialog.dialog.dismiss()
                hideDialog()

            }

        })

        //observe if product was inserted
        viewModel.wasProductInserted.observe(viewLifecycleOwner, Observer { wasProductInserted ->
            if (wasProductInserted) {
                //alert the user the product was inserted
                Toast.makeText(requireContext(), "Product was saved", Toast.LENGTH_LONG).show()
                // viewModel.doneInsertingProduct()
            } else {
                //alert the user the product failed to save
                Toast.makeText(requireContext(), "Product failed to save", Toast.LENGTH_LONG).show()
            }
        })
        //listen for when add button is clicked
        viewModel.isAddButtonClicked.observe(viewLifecycleOwner, Observer { isAddButtonClicked ->
            if (isAddButtonClicked) {
                HideKeyBoard.hidKeyBoard(this.requireActivity())
            }
        })
        //listen for when fields are empty
        viewModel.areFieldsEmpty.observe(viewLifecycleOwner, Observer { areFieldsEmpty ->
            if (areFieldsEmpty) {
                //alert the user to fill the fields
                Toast.makeText(requireContext(), "Fill all fields!", Toast.LENGTH_LONG).show()
            }
        })


        return mBinding.root
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //check for the request code
//        if (resultCode == Activity.RESULT_OK) {
//            Log.i(TAG, "Result: OK")
//            //check if the result is ok
//            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
//                Log.i(TAG, "REQUEST CODE = $PICK_IMAGE_REQUEST_CODE")
//                //get the uri of the image
//                var imageUrl: Uri? = data?.data
//                // val imageBitMap = data!!.extras!!.get("data") as Bitmap
//                //display the side indicator to show where the image was got from
//                Picasso.get().setIndicatorsEnabled(true)
//                //load the image selected into the image view
//                Picasso.get().load(imageUrl).fit().centerCrop().into(mBinding.newProductImage)
//                FirebaseStorage.getInstance().reference.child("product_pics").putFile(imageUrl!!)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.i(TAG, "Picture: Inserted into fb")
//                        } else {
//                            Log.i(TAG, task.exception!!.message)
//                        }
//                    }
//                // viewModel.getImageUri(imageUrl)
//                Toast.makeText(context, "Image Saved!", Toast.LENGTH_LONG).show()
//            } else if (requestCode == SAVE_IMAGE_REQUEST_CODE) {
//                Log.i(TAG, "REQUEST CODE = $SAVE_IMAGE_REQUEST_CODE")
//                //get the image uri
//                var imageUrl = data!!.data
//                FirebaseStorage.getInstance().reference.child("product_pics").putFile(imageUrl!!)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.i(TAG, "Picture: Inserted into fb")
//                        } else {
//                            Log.i(TAG, task.exception!!.message)
//                        }
//                    }
//                //display the image on the image view
//                Picasso.get().load(imageUrl).fit().into(mBinding.newProductImage)
//                //  viewModel.getImageUri(imageUrl)
//                //tell the user it has been saved
//                Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show()
//            }
//        } else {
//            //toast a message to the user that image is not gotten
//            Toast.makeText(context, "Image not gotten!", Toast.LENGTH_LONG).show()
//        }
//    }

    override fun getImagePath(imagePath: Uri) {
        //check if the uri is no empty
        if (imagePath.toString() != "") {

            //use Picasso to display the image on the imageView
            Picasso.get().load(imagePath).fit().centerCrop().into(mBinding.newProductImage)
            //if true, set the bitmap to null
            mSelectedImageBitMap = null
            //set the uri to the given uri
            mSelectedImageUri = imagePath
            viewModel.getImageUri(imagePath)
            FirebaseStorage.getInstance().reference.child("product_pic").putFile(imagePath)
                .addOnCompleteListener {
                    val result = it.result!!.storage.root
                    Log.i(TAG, result.toString())
                }
            Log.i(TAG, "getImagePath: got the image Uri $mSelectedImageUri")


        }

    }

    override fun getImageBitmap(bitmap: Bitmap) {
        //check if bitmap is not null
        if (bitmap != null) {
            //convert the bitMap to uri so Picasso can display it
            val uri = convertBitmapToUri(bitmap)
            //display the image with Picasso on the imageView
            Picasso.get().load(uri).fit().centerCrop().into(mBinding.newProductImage)
            viewModel.getImageUri(uri)

            //if not null set the uri to null
            mSelectedImageUri = null
            //set the bitmap to the given bitmap
            mSelectedImageBitMap = bitmap

            FirebaseStorage.getInstance().reference.child("product_pic").putFile(uri)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val result = it.result!!.storage.root
                        Log.i(TAG, it.result!!.storage.root.toString())

                    } else {
                    }
                }
            Log.i(TAG, "getImageBitmap: got the image bitmap $mSelectedImageBitMap")

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
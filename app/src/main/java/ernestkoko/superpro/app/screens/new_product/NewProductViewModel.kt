package ernestkoko.superpro.app.screens.new_product

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import ernestkoko.superpro.app.firebase.Product

class NewProductViewModel : ViewModel() {
    private val TAG = "NewProdViewMod"
    private val databaseRef = Firebase.database.reference
    private val mAuth = FirebaseAuth.getInstance()
    private var mImageUri: Uri? = Uri.EMPTY


    //product name
    var productName = MutableLiveData<String>()
        set(value) {
            productName.value = value.value
        }

    //user password2
    var manufacturer = MutableLiveData<String>()
        set(value) {
            manufacturer.value = value.value
        }

    //user password2
    var quantity = MutableLiveData<String>()
        set(value) {
            quantity.value = value.value
        }
    private val _isImageClicked = MutableLiveData<Boolean>()
    val isImageClicked: LiveData<Boolean>
        get() = _isImageClicked
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean>
        get() = _showDialog
    private val _wasProductInserted = MutableLiveData<Boolean>()
    val wasProductInserted: LiveData<Boolean>
        get() = _wasProductInserted

    fun onAddButtonClicked() {
        //show dialog
        _showDialog.value = true
        if (mImageUri != null && mImageUri != Uri.EMPTY) {
            FirebaseStorage.getInstance().reference.child("product_pics")
                .child(mAuth.currentUser!!.uid)
                .putFile(mImageUri!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val imageUri = task.result!!.uploadSessionUri
                        Log.i(TAG, "ImageUpload Uri: ${imageUri.toString()}")
//                        val product = Product(
//                            productName.value!!.toString().trim(),
//                            quantity.value!!.toString().trim(),
//                            manufacturer.value!!.toString().trim(),
//                            imageUri.toString()
//                        )
//                        Log.i(TAG, "Add Button: Clicked")
//                        //insert the new product to friebase database
//                        databaseRef.child("product").child(mAuth.currentUser!!.uid).push()
//                            .setValue(product).addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    Log.i(TAG, "Product: Inserted successfully")
//                                    _wasProductInserted.value = true
//                                } else {
//                                    //task failed
//                                    Log.i(TAG, "Product: insertion failed")
//                                    _wasProductInserted.value = false
//                                }
//                            }

                    } else {
                        Log.i(TAG, "ImageUploadTask: Unsuccessful")
                    }
                    //remove the dialog
                    _showDialog.value = false
                }
        } else {
            val product = Product(
                productName.value!!.toString().trim(),
                quantity.value!!.toString().trim(),
                manufacturer.value!!.toString().trim()
            )
            Log.i(TAG, "Add Button: Clicked")
            //insert the new product to friebase database
            databaseRef.child("product").child(mAuth.currentUser!!.uid).push()
                .setValue(product).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(TAG, "Product: Inserted successfully")
                        _wasProductInserted.value = true
                    } else {
                        //task failed
                        Log.i(TAG, "Product: insertion failed")
                        _wasProductInserted.value = false
                    }
                }

        }


    }

    //fun called when image view is clicked
    fun imageClicked() {
        Log.i(TAG, "Image View: clicked")
        _isImageClicked.value = true

    }

    fun finishWithPicture() {
        Log.i(TAG, "Done with ImageView")
        _isImageClicked.value = false

    }

    //get the image uri
    fun getImageUri(uri: Uri?) {
        Log.i(TAG, "getImageUri: Called")
        mImageUri = uri
    }

    fun doneInsertingProduct() {
        _wasProductInserted.value = false
    }
}
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
import com.google.firebase.storage.ktx.storage
import ernestkoko.superpro.app.firebase.Product

class NewProductViewModel : ViewModel() {
    private val TAG = "NewProdViewMod"
    private val databaseRef = Firebase.database.reference
    private val mAuth = FirebaseAuth.getInstance()
    private var mImageUri: Uri? = Uri.EMPTY
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private var mNodeKey: String? = null

    private val mUser = mAuth.currentUser


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
    private val _isAddButtonClicked = MutableLiveData<Boolean>()
    val isAddButtonClicked: LiveData<Boolean>
        get() = _isAddButtonClicked
    private val _areFieldsEmpty = MutableLiveData<Boolean>()
    val areFieldsEmpty: LiveData<Boolean>
        get() = _areFieldsEmpty

    init {

    }

    fun onAddButtonClicked() {
        //get the current time
        val currentTime = System.currentTimeMillis().toString()

        _isAddButtonClicked.value = true
        if (!productName.value?.trim().isNullOrEmpty() && !manufacturer.value?.trim()
                .isNullOrEmpty() && !quantity.value?.trim().isNullOrEmpty()
        ) {
            //fields are not empty
            //show dialog
            _showDialog.value = true
            _areFieldsEmpty.value = false

            //push to the node and get the key
            val pushNode =databaseRef.child("product").child(mAuth.currentUser!!.uid).push()
            mNodeKey = pushNode.key
            //check if the image uri is null or empty
            if (mImageUri != null && mImageUri != Uri.EMPTY) {
                Log.i(TAG, "ImageUri is not null")
                Log.i(TAG, "ImageUrl: ${mImageUri.toString()}")
                    val ref = mStorageRef.child("products_pictures")
                        .child(mAuth.currentUser!!.uid).child("pics_${currentTime}")
                val uploadTask = ref.putFile(mImageUri!!)
               .continueWithTask { task ->
                    if (!task.isComplete) {
                        task.exception?.let {exception ->
                            throw exception
                        }

                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                        // Log.i(TAG, "Image Upload: Successful")
                        if (task.isSuccessful) {

                            val imageUri = task.result
                            Log.i(TAG, "ImageUpload Uri: ${imageUri.toString()}")
//                            //insert the new product to firebase database
//                            val pushNode =databaseRef.child("product").child(mAuth.currentUser!!.uid).push()
//                            mNodeKey = pushNode.key
                            Log.i(TAG,"Push key is: ${ mNodeKey}")
                            //product to insert into the firebase db
                            val product = Product(
                                mNodeKey,
                                productName.value!!.toString().trim(),
                                quantity.value!!.toString().trim(),
                                manufacturer.value!!.toString().trim(),
                                imageUri.toString(),
                            )
                            Log.i(TAG, "Add Button: Clicked")
                            // finally inserting it into firebase db
                                 pushNode.setValue(product).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.i(TAG, "Product: Inserted successfully")
                                        _wasProductInserted.value = true
                                        _showDialog.value = false
                                    } else {
                                        //task failed
                                        Log.i(TAG, "Product: insertion failed")
                                        _wasProductInserted.value = false
                                        _showDialog.value = false
                                    }
                                }

                        } else {
                            Log.i(TAG, task.exception?.message.toString())

                            Log.i(TAG, "ImageUploadTask: Unsuccessful")
                        }
                        // remove the dialog
                        _showDialog.value = false
                    }.addOnFailureListener {
                        Log.i(TAG, it.message.toString())
                        //remove the dialog
                        _showDialog.value = false

                    }
            } else {
                Log.i(TAG, "ImageUri: null")
                val product = Product(
                    mNodeKey,
                    productName.value!!.toString().trim(),
                    quantity.value!!.toString().trim(),
                    manufacturer.value!!.toString().trim()
                )

                Log.i(TAG, "Add Button: Clicked")
                //insert the new product to firebase database
                pushNode.setValue(product).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i(TAG, "Product: Inserted successfully")
                            _wasProductInserted.value = true
                            _showDialog.value = false
                        } else {
                            //task failed
                            Log.i(TAG, "Product: insertion failed")
                            _wasProductInserted.value = false
                            _showDialog.value = false
                        }
                    }

            }
        } else {
            //fields are empty
            _areFieldsEmpty.value = true
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


}
package ernestkoko.superpro.app.screens.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ernestkoko.superpro.app.firebase.Product

class ProductDetailsViewModel : ViewModel() {
    private val mFirebaseDbRef = Firebase.database
    private val mFirebaseAuth = FirebaseAuth.getInstance()

    private val _prodName = MutableLiveData<String?>()
    val prodName: LiveData<String?>
    get() = _prodName
    private val _prodManufacturer = MutableLiveData<String?>()
    val prodManufacturer
    get() =  _prodManufacturer


    fun getTheArgument(productId: String?) {
        //get the user id
       val userId = mFirebaseAuth.currentUser!!.uid
        mFirebaseDbRef.getReference("product/${userId}/${productId}").orderByKey()
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get the product from the snapshot
                    val product = snapshot.getValue(Product::class.java)
                    //set the product name
                    _prodName.value = product!!.name
                    //set the manufacturer
                    _prodManufacturer.value = product!!.manufacturer


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

}
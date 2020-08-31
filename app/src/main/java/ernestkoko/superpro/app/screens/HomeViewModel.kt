package ernestkoko.superpro.app.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ernestkoko.superpro.app.firebase.Product

class HomeViewModel : ViewModel() {
    private val TAG = "HomeViewModel"
    private val databaseRef = Firebase.database.reference

    var mProducts: MutableList<Product>
    val mProd = MutableLiveData<List<String>>()
    var prod = MutableLiveData<List<Product>>()
    private val mDatabase = FirebaseDatabase.getInstance()
    private lateinit var mDatabaseRef: DatabaseReference
    private val mUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    var listProd= mutableListOf<Product>()

    private fun listenToProducts(){
       // databaseRef.child("product").child(mUser.uid).addValueEventListener()

    }




    init {
        mDatabaseRef = mDatabase.reference
        mProducts = mutableListOf()

        mDatabaseRef.child("product").child(mUser.uid)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                   Log.i(TAG,"Size:"+ snapshot.getValue())

                    val product = snapshot.getValue(Product::class.java)
                    Log.i(TAG, "Child: Added to list")
                    mProducts.add(product!!)

                    Log.i(TAG, listProd.size.toString())
                    listProd.add(product)


                    Log.i(TAG,"NewProd: " + product.toString())
                   prod.value = listProd

                   // notifyItemInserted(mProducts.size - 1)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

            })

    }






    //observable for navigating to newProduct fragment
    private val _navToNewProduct = MutableLiveData<Boolean>()
    val navToNewProduct: LiveData<Boolean>
    get() = _navToNewProduct
    private val _isLogoutClicked = MutableLiveData<Boolean>()
    val isLogoutClicked: LiveData<Boolean>
    get() = _isLogoutClicked

    fun onFabClicked(){
        Log.i(TAG, "Fab: clicked")
        _navToNewProduct.value = true
    }
    fun doneNavToNewProduct(){
        Log.i(TAG,"Done nav to new Prod Frag")
        _navToNewProduct.value = false
    }
    //fun called when logout is clicked
    fun isLogoutLayoutClicked(){

        _isLogoutClicked.value = true
    }
    fun doneClickingLogout(){
        _isLogoutClicked.value = false
    }

    private val _navigateToProductDetails = MutableLiveData<String>()
    val navigateToProductDetails: LiveData<String>
    get() = _navigateToProductDetails

    fun onProductClicked(productId: String) {
        _navigateToProductDetails.value = productId
    }
    fun onProductDetailsNavigated(){
        _navigateToProductDetails.value = null
    }
}
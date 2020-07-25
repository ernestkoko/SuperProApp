package ernestkoko.superpro.app.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val TAG = "HomeViewModel"

    //observable for navigating to newProduct fragment
    private val _navToNewProduct = MutableLiveData<Boolean>()
    val navToNewProduct: LiveData<Boolean>
    get() = _navToNewProduct

    fun onFabClicked(){
        Log.i(TAG, "Fab: clicked")
        _navToNewProduct.value = true
    }
    fun doneNavToNewProduct(){
        Log.i(TAG,"Done nav to new Prod Frag")
        _navToNewProduct.value = false
    }
}
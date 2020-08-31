package ernestkoko.superpro.app.screens.product_details


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.ProductDetailsFragmentBinding
import ernestkoko.superpro.app.firebase.Product

class ProductDetailsFragment : Fragment() {
    private val TAG = "ProdDetailsFrag"
    private lateinit var mBinding: ProductDetailsFragmentBinding
    //get the argument that was passed to it
    private val args: ProductDetailsFragmentArgs by navArgs()

     val mFirebase = Firebase.database 

    companion object {
        fun newInstance() =
            ProductDetailsFragment()
    }

    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.product_details_fragment,container,false)
        Log.i(TAG, args.productId.toString())
        //initialise the viewModel
        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)



//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        Log.i(TAG, userId)
//
//        mFirebase.getReference("product/${userId}/${args.productId}").orderByKey()
//            .addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.i(TAG,snapshot.toString())
//                val snap = snapshot.value
//                Log.i(TAG, snap.toString())
//
//                val prod = snapshot.getValue(Product::class.java)
//                Log.i(TAG, prod.toString())
//                Log.i(TAG,"Name:"+prod!!.name.toString())
//                mBinding.productNameText.text = "Name: "+prod!!.name
//                val p = snapshot.getValue<Product>()
//                Log.i(TAG, p.toString())
////                Log.i(TAG, "name:"+p!!.name.toString())
////                Log.i(TAG,"${prod?.name.toString()}, ${prod?.quantity.toString()}, ${prod!!.manufacturer}")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.i(TAG, error.message)
//            }
//        })





      //  mBinding.productNameText.text = args.productId.toString()





        //find the nav controller
        val navController = findNavController()
        //get the toolbar
        val toolBar = mBinding.productDetailsToolBar
        //set the tool bar up with the nav Controller
        toolBar.setupWithNavController(navController)
        //get the menu from the tool bar
        val menu = toolBar.menu
        //get each menu item
        menu.forEach { menuItem ->
            //set on click listener on the items
            menuItem.setOnMenuItemClickListener(object :MenuItem.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when(item!!.itemId){
                        R.id.edit_product -> Log.i(TAG, "Edit: Clicked")
                        R.id.delete_product -> Log.i(TAG, "Delete: Clicked")
                    }

                    //return true
                    return true
                }
            })
        }


        //wire up the view model
        mBinding.productDetailsViewModel = viewModel
        //pass the arg to the viewModel
        viewModel.getTheArgument(args.productId)

        //observe when to set the name
        viewModel.prodName.observe(viewLifecycleOwner, Observer { name ->
            //set the name to the gotten string
            mBinding.productNameText.text = "Name: ${name!!}"
        })
        //observe to set manufacturer
        viewModel.prodManufacturer.observe(viewLifecycleOwner, Observer { manufacturer ->
            //set it to the textView
            mBinding.productManufacturerText.text = "Manufacturer: ${manufacturer!!}"
        })


        //bind it to the root and return it
        return mBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)

    }

}
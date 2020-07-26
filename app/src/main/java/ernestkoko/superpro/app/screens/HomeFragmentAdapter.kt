package ernestkoko.superpro.app.screens

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ernestkoko.superpro.app.databinding.ProductListViewBinding
import ernestkoko.superpro.app.firebase.Product

class HomeFragmentAdapter : RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {
    private lateinit var mProducts: MutableList<Product>
    private val mDatabase = FirebaseDatabase.getInstance()
    private lateinit var mDatabaseRef: DatabaseReference
    private val mUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val TAG = "HomeFragAdapter"


    init {
        mDatabaseRef = mDatabase.reference
        mProducts = mutableListOf()
        mDatabaseRef.child("product").child(mUser.uid)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val product = snapshot.getValue(Product::class.java)
                    Log.i(TAG, "Child: Added to list")
                    mProducts.add(product!!)
                    notifyItemInserted(mProducts.size - 1)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

            })

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductListViewBinding.inflate(inflater, parent, false)


        return HomeFragmentViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return mProducts.size
    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
          holder.bind(mProducts.get(position))
    }


    class HomeFragmentViewHolder(val binding: ProductListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //  private val prodImage = view.findViewById<TextView>(R.id.)
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productManufacturer.text = product.manufacturer

        }

    }
}
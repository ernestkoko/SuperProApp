package ernestkoko.superpro.app.screens

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import ernestkoko.superpro.app.databinding.ProductListViewBinding
import ernestkoko.superpro.app.firebase.Product

class HomeFragmentAdapter : ListAdapter<Product, HomeFragmentAdapter.HomeFragmentViewHolder>(ProductDiffCallback()) {
//    private lateinit var mProducts: MutableList<Product>
//    private val mDatabase = FirebaseDatabase.getInstance()
//    private lateinit var mDatabaseRef: DatabaseReference
//    private val mUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val TAG = "HomeFragAdapter"


//    init {
//        mDatabaseRef = mDatabase.reference
//        mProducts = mutableListOf()
//        mDatabaseRef.child("product").child(mUser.uid)
//            .addChildEventListener(object : ChildEventListener {
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//
//                }
//
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    val product = snapshot.getValue(Product::class.java)
//                    Log.i(TAG, "Child: Added to list")
//                    mProducts.add(product!!)
//                    notifyItemInserted(mProducts.size - 1)
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//
//                }
//
//            })
//
//    }
//



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {

//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ProductListViewBinding.inflate(inflater, parent, false)


        return HomeFragmentViewHolder.from(parent)

    }

//    override fun getItemCount(): Int {
//        return mProducts.size
//    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
       // holder.bind(mProducts.get(position))
    }


    class HomeFragmentViewHolder private constructor(val binding: ProductListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //  private val prodImage = view.findViewById<TextView>(R.id.)
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
//            binding.productName.text = product.name
//            binding.productManufacturer.text = product.manufacturer
//            binding.productQuantity.text = product.quantity
//            showImage(product.image_url!!)

        }
        companion object{
            fun from(parent: ViewGroup): HomeFragmentViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductListViewBinding.inflate(layoutInflater, parent, false)
                return  HomeFragmentViewHolder(binding)
            }
        }

//        private fun showImage(url: String) {
//            Log.i("ShowImage", "ShowImage: called")
//            Log.i("ShowImage", "ImageUrl: ${url}")
//            if (!url.isNullOrEmpty()) {
//                Log.i("ShowImage", "Image Url not null")
//               // Picasso.get().load(itemView.context.resources.dr)
//                Glide.with(binding.productImage.context).load(url)
//                    .apply(
//                        RequestOptions()
//                            .placeholder(R.drawable.notification_bg)
//                            .error(R.drawable.common_google_signin_btn_icon_light_normal)
//                    )
//
//                    .override(100, Target.SIZE_ORIGINAL).fitCenter().into(binding.productImage)
//            } else {
//                Log.i("ShowImage", "Image Url is null")
//            }
//        }

    }
}
/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
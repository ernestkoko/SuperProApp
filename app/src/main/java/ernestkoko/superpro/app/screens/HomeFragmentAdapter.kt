package ernestkoko.superpro.app.screens

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val product = snapshot.getValue(Product::class.java)
                    Log.i(TAG, "Child: Added to list")
                    mProducts.add(product!!)
                    notifyItemInserted(mProducts.size - 1)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

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
            binding.productQuantity.text = product.quantity
            showImage(product.image_url!!)

        }

        private fun showImage(url: String) {
            Log.i("ShowImage", "ShowImage: called")
            Log.i("ShowImage", "ImageUrl: ${url}")
            if (!url.isNullOrEmpty()) {
                Log.i("ShowImage", "Image Url not null")
//                Picasso.get()
//                    .load(url)
//                    .fit()
//                    .into(binding.productImage)
                Glide.with(binding.productImage.context).load(url)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.notification_bg)
                            .error(R.drawable.common_google_signin_btn_icon_light_normal)
                    )

                    .override(100, Target.SIZE_ORIGINAL).fitCenter().into(binding.productImage)
            } else {
                Log.i("ShowImage", "Image Url is null")
            }
        }

    }
}
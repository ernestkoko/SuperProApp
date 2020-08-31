package ernestkoko.superpro.app.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ernestkoko.superpro.app.databinding.ProductListViewBinding
import ernestkoko.superpro.app.firebase.Product

class HomeFragmentAdapter(val clickListener: ProductClickListener) : ListAdapter<Product, HomeFragmentAdapter.HomeFragmentViewHolder>(ProductDiffCallback()) {
    private val TAG = "HomeFragAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        return HomeFragmentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(getItem(position)!!, clickListener)
    }
    /**
     * View Holder class that holds the view for the adapter
     * @param binding that is the data binding class
     */
    class HomeFragmentViewHolder private constructor(val binding: ProductListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //  private val prodImage = view.findViewById<TextView>(R.id.)
        fun bind(product: Product, clickListener: ProductClickListener) {
            binding.product = product
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): HomeFragmentViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductListViewBinding.inflate(layoutInflater, parent, false)
                return  HomeFragmentViewHolder(binding)
            }
        }
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
        return oldItem.product_id == newItem.product_id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
class ProductClickListener(val clickListener: (productId: String) -> Unit){
     fun onClick(product: Product) = clickListener(product.product_id!!)
}
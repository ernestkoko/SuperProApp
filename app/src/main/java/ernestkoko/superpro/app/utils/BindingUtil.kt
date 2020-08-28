package ernestkoko.superpro.app.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.firebase.Product

@BindingAdapter("productName")
fun TextView.setProductName(item: Product?){
    item?.let {
        text = item.name
    }
}
@BindingAdapter("productManufacturer")
fun TextView.setProductManufacturer(item: Product?){
    item?.let {
        text = item.manufacturer
    }
}
@BindingAdapter("productImage")
fun ImageView.setProductImage(item: Product?){
    item?.let {
        setImageResource(R.drawable.ic_deault_pic)
    }
}
@BindingAdapter("prodQuantity")
fun TextView.setQuantity(item: Product?){
    item?.let {
        text = item.quantity
    }
}

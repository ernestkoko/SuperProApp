package ernestkoko.superpro.app.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Picasso
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
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    if (!url.isNullOrEmpty()){
        Picasso.get().load(url).error(R.drawable.ic_deault_pic)
            .placeholder(R.drawable.ic_deault_pic).fit().centerCrop().into(view)
//        Glide.with(view).load(url).apply(
//                        RequestOptions()
//                            .placeholder(R.drawable.ic_deault_pic)
//                            .error(R.drawable.ic_deault_pic)
//                    )
//
//                    .override(100, Target.SIZE_ORIGINAL).fitCenter().into(view)
   }

}


@BindingAdapter("prodQuantity")
fun TextView.setQuantity(item: Product?){
    item?.let {
        text = item.quantity
    }
}

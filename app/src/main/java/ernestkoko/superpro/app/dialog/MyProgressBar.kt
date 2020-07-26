package ernestkoko.superpro.app.dialog

import android.app.Dialog
import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import ernestkoko.superpro.app.R

class MyProgressBar(context: Context): Dialog(context) {
   private lateinit var  mDialog: Dialog

    init {
        mDialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
    }


        fun showProgressDialog(){
         mDialog.setContentView(R.layout.my_progress_bar)
            mDialog.setCancelable(false)
                mDialog.show()
        }
    fun hideProgressDialog(){
        mDialog.dismiss()
    }
}
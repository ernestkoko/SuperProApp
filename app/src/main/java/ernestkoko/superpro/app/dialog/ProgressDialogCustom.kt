package ernestkoko.superpro.app.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.DialogChangePhotoBinding
import ernestkoko.superpro.app.databinding.ProgessDialogViewBinding

class ProgressDialogCustom : DialogFragment() {
    private lateinit var mBinding: ProgessDialogViewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.progess_dialog_view, container, false)

        mBinding.cpPbar.isIndeterminate = true
        mBinding.cpPbar.indeterminateDrawable
        return mBinding.root
    }
}
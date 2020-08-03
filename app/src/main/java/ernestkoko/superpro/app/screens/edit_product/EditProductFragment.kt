package ernestkoko.superpro.app.screens.edit_product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ernestkoko.superpro.app.R

class EditProductFragment : Fragment() {

    companion object {
        fun newInstance() =
            EditProductFragment()
    }

    private lateinit var viewModel: EdithProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edith_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EdithProductViewModel::class.java)

    }

}
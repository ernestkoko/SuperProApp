package ernestkoko.superpro.app.screens.new_product

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ernestkoko.superpro.app.R

class NewProductFragment : Fragment() {

    companion object {
        fun newInstance() =
            NewProductFragment()
    }

    private lateinit var viewModel: NewProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

}